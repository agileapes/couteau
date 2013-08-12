package com.agileapes.couteau.maven.mojo;

import com.agileapes.couteau.basics.api.Filter;
import com.agileapes.couteau.basics.api.Processor;
import com.agileapes.couteau.basics.api.Transformer;
import com.agileapes.couteau.maven.resource.ClassPathScanningClassProvider;
import com.agileapes.couteau.maven.resource.ClassPathScope;
import com.agileapes.couteau.maven.resource.ConfigurableClassLoader;
import com.agileapes.couteau.maven.resource.ProjectResource;
import com.agileapes.couteau.maven.resource.impl.ClassPathScopeArtifactFilter;
import com.agileapes.couteau.maven.task.PluginTask;
import com.agileapes.couteau.maven.task.TaskScheduler;
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

    private Collection<ProjectResource> projectResources = null;

    /**
     * The project class loader is supposed to be able to lookup classes from the target project. This field
     * is lazily loaded via {@link #getProjectClassLoader()}
     */
    private ClassLoader projectClassLoader = null;

    private final List<Filter<Artifact>> artifactFilters = new ArrayList<Filter<Artifact>>();

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

    public synchronized Collection<ProjectResource> getProjectResources() {
        if (projectResources == null) {
            fetchProjectResources();
        }
        return projectResources;
    }

    protected abstract Collection<PluginTask<?>> getTasks();

    public TaskScheduler getTaskScheduler() {
        final TaskScheduler scheduler = new TaskScheduler(getWorkers());
        scheduler.setPluginExecutor(this);
        return scheduler;
    }

    protected int getWorkers() {
        return 10;
    }

    protected AbstractPluginExecutor() {
        addArtifactFilter(new ClassPathScopeArtifactFilter(ClassPathScope.COMPILE));
    }

    public abstract MavenProject getProject();

    public abstract Set<String> getScanPackages();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassUtils.overrideThreadContextClassLoader(getProjectClassLoader());
        final TaskScheduler taskScheduler = getTaskScheduler();
        with(getTasks()).each(new Processor<PluginTask<?>>() {
            @Override
            public void process(PluginTask<?> input) {
                taskScheduler.schedule(input);
            }
        });
        taskScheduler.start();
        try {
            taskScheduler.join();
        } catch (InterruptedException e) {
            throw new MojoExecutionException("Failed to carry out tasks properly", e);
        }
    }

}
