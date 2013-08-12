/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.couteau.lang.model;

import com.agileapes.couteau.freemarker.api.Template;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/22/13, 3:04 PM)
 */
@Template("ftl/pojo.ftl")
public class JavaPojoModel {

    private static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(new String[]{"public", "private", "protected",
            "package", "final", "char", "class", "int", "boolean", "long", "short", "import", "if",
            "else", "do", "while", "for", "this", "return", "interface", "volatile", "transient",
            "new", "super"}));

    protected final String qualifiedName;
    protected final String packageName;
    protected final String simpleName;
    private final Set<String> imports = new HashSet<String>();
    private final Map<String, String> properties = new HashMap<String, String>();

    public JavaPojoModel(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        if (qualifiedName.indexOf('.') != -1) {
            packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
            simpleName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
        } else {
            packageName = "";
            simpleName = qualifiedName;
        }
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public Set<String> getImports() {
        return Collections.unmodifiableSet(imports);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void addImport(String type) {
        //primitive types are ignored
        if (type.indexOf('.') == -1 && type.toLowerCase().equals(type)) {
            return;
        }
        //types under java.lang are ignored
        if (type.matches("java\\.lang\\.[^\\.]+")) {
            return;
        }
        //types under the same package as this class are ignored
        if (type.matches(getPackageName().replace(".", "\\.") + "\\.[^\\.]+")) {
//            return;
        }
        imports.add(type);
    }

    public void addProperty(String property, Class<?> type) {
        addProperty(property, type.getCanonicalName());
    }

    public void addProperty(String property, String type) {
        addImport(type);
        while (KEYWORDS.contains(property)) {
            property = "_" + property;
        }
        properties.put(property, !type.contains(".") ? type : type.substring(type.lastIndexOf('.') + 1));
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
