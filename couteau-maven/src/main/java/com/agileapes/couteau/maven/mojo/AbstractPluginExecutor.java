package com.agileapes.couteau.maven.mojo;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.basics.api.Transformer;
import com.agileapes.couteau.basics.api.impl.StaticStringifiable;
import com.agileapes.couteau.concurrency.manager.impl.ThreadPoolTaskManager;
import com.agileapes.couteau.graph.alg.sort.impl.TopologicalGraphSorter;
import com.agileapes.couteau.graph.node.impl.DirectedNode;
import com.agileapes.couteau.maven.resource.ClassPathScanningClassProvider;
import com.agileapes.couteau.maven.resource.ClassPathScope;
import com.agileapes.couteau.maven.resource.ConfigurableClassLoader;
import com.agileapes.couteau.maven.resource.ProjectResource;
import com.agileapes.couteau.maven.resource.impl.ClassPathScopeArtifactFilter;
import com.agileapes.couteau.maven.task.PluginTask;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.agileapes.couteau.basics.collections.CollectionWrapper.with;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (8/2/13, 10:55 AM)
 */
public abstract class AbstractPluginExecutor extends AbstractMojo {

    /**
     * This field will act as a cache for project resources, once they have been
     * loaded through the plugin
     */
    private Collection<ProjectResource> projectResources = null;

    /**
     * The project class loader is supposed to be able to lookup classes from the target project. This field
     * is lazily loaded via {@link #getProjectClassLoader()}
     */
    private ClassLoader projectClassLoader = null;

    /**
     * These are the filters that will determine which artifacts should be scanned for
     * project resources
     */
    private final List<Filter<Artifact>> artifactFilters = new ArrayList<Filter<Artifact>>();

    /**
     * Adds a new artifact filter
     * @param filter    the filter to be added
     */
    protected void addArtifactFilter(Filter<Artifact> filter) {
        artifactFilters.add(filter);
    }

    /**
     * This method will try to get a class loader with access to the target project's classes and dependencies
     *
     * @return the class loader
     */
    public ClassLoader getProjectClassLoader() {
        if (projectClassLoader != null) {
            return projectClassLoader;
        }
        ClassLoader projectClassLoader;
        try {
            List<String> classpathElements = new ArrayList<String>();
            final Set dependencyArtifacts = getProject().getDependencyArtifacts();
            for (Object object : dependencyArtifacts) {
                if (object instanceof Artifact) {
                    Artifact artifact = (Artifact) object;
                    boolean add = true;
                    for (Filter<Artifact> filter : artifactFilters) {
                        if (!filter.accepts(artifact)) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        classpathElements.add(artifact.getFile().getAbsolutePath());
                    }
                }
            }
            classpathElements.add(getProject().getBuild().getOutputDirectory());
            URL urls[] = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            projectClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            getLog().warn("Failed to setup project class loader");
            projectClassLoader = this.getClass().getClassLoader();
        }
        return this.projectClassLoader = new ConfigurableClassLoader(projectClassLoader);
    }

    /**
     * This method will return all project resources
     *
     * @return the project resources
     */
    private Set<Resource> getResources() {
        final Set<Resource> resources = new HashSet<Resource>();
        try {
            Collections.addAll(resources, new PathMatchingResourcePatternResolver(getProjectClassLoader()).getResources("classpath*:*"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load project resources", e);
        }
        return resources;
    }

    /**
     * This method will fetch project resource objects
     */
    private void fetchProjectResources() {
        final Set<Class> classes = getClasses();
        final Set<Resource> resources = getResources();
        projectResources = new CopyOnWriteArraySet<ProjectResource>();
        projectResources.addAll(with(classes).transform(new Transformer<Class, ProjectResource>() {
            @Override
            public ProjectResource map(Class input) {
                return new ProjectResource(input);
            }
        }).list());
        projectResources.addAll(with(resources).transform(new Transformer<Resource, ProjectResource>() {
            @Override
            public ProjectResource map(Resource input) {
                return new ProjectResource(input);
            }
        }).list());
    }

    /**
     * This method will return a set of all project classes
     *
     * @return the classes
     */
    protected Set<Class> getClasses() {
        final ClassPathScanningClassProvider scanner = new ClassPathScanningClassProvider(true);
        scanner.setClassLoader(getProjectClassLoader());
        scanner.addIncludeFilter(new AssignableTypeFilter(Object.class));
        final Set<Class> candidateClasses = new LinkedHashSet<Class>();
        with(getScanPackages()).each(new Processor<String>() {
            @Override
            public void process(String input) {
                candidateClasses.addAll(scanner.findCandidateClasses(input));
            }
        });
        return candidateClasses;
    }

    /**
     * This method will fetch project resources, or if they have already been fetched, will
     * simply return them from the cache
     * @return a collection of all project resources
     */
    public synchronized Collection<ProjectResource> getProjectResources() {
        if (projectResources == null) {
            fetchProjectResources();
        }
        return projectResources;
    }

    /**
     * @return the number of worker threads spawned by the plugin to manage concurrent
     * execution of tasks. This method is simply here so that this value might be parameterized
     * in extending subclasses. The default value is {@link Runtime#availableProcessors()}
     */
    protected int getWorkers() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Instantiates the plugin by including all compile scope artifacts
     */
    protected AbstractPluginExecutor() {
        addArtifactFilter(new ClassPathScopeArtifactFilter(ClassPathScope.COMPILE));
    }

    /**
     * Topologically sorts the given tasks based on their dependencies
     * @param tasks    unsorted tasks
     * @return a list of tasks, sorted by their dependencies
     */
    private List<PluginTask<?>> sortTasks(Collection<PluginTask<?>> tasks) {
        final Map<PluginTask<?>, DirectedNode> nodes = new HashMap<PluginTask<?>, DirectedNode>();
        for (PluginTask<?> task : tasks) {
            final DirectedNode node = new DirectedNode(new StaticStringifiable<DirectedNode>(task.getName()));
            nodes.put(task, node);
            node.setUserData("task", task);
        }
        for (PluginTask<?> task : tasks) {
            final DirectedNode node = nodes.get(task);
            for (PluginTask pluginTask : task.getDependencies()) {
                node.addNeighbor(nodes.get(pluginTask));
            }
        }
        final List<DirectedNode> directedNodes = new TopologicalGraphSorter().sort(nodes.values());
        final ArrayList<PluginTask<?>> result = new ArrayList<PluginTask<?>>();
        for (DirectedNode node : directedNodes) {
            result.add((PluginTask<?>) node.getUserData("task"));
        }
        return result;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassUtils.overrideThreadContextClassLoader(getProjectClassLoader());
        final List<PluginTask<?>> tasks = sortTasks(getTasks());
        final ThreadPoolTaskManager taskManager = new ThreadPoolTaskManager("TaskManager", getWorkers(), true);
        for (PluginTask task : tasks) {
            //noinspection unchecked
            task.setPluginExecutor(this);
            taskManager.schedule(task);
        }
        final Thread taskManagerThread = new Thread(taskManager);
        taskManagerThread.start();
        try {
            taskManagerThread.join();
        } catch (InterruptedException e) {
            throw new MojoExecutionException("Failed to carry out tasks properly", e);
        }
    }

    /**
     * @return the Maven project being processed
     */
    public abstract MavenProject getProject();

    /**
     * @return package prefixes to scan for classes
     */
    public abstract Set<String> getScanPackages();

    /**
     * @return the tasks the plugin is supposed to execute
     */
    protected abstract Collection<PluginTask<?>> getTasks();

}
