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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (5/22/13, 2:31 PM)
 */
public class MethodModel {

    private final String name;
    private final String returnType;
    private final List<ParameterModel> parameters = new ArrayList<ParameterModel>();
    private final List<String> body = new ArrayList<String>();
    private String visibility = "public";

    public MethodModel(String name, String returnType, ParameterModel... parameters) {
        this.name = name;
        this.returnType = returnType;
        Collections.addAll(this.parameters, parameters);
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public void addLine(String line) {
        body.add(line);
    }

    public List<String> getBody() {
        return body;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

}
