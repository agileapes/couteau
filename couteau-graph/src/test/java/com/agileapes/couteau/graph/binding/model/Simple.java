package com.agileapes.couteau.graph.binding.model;

import com.agileapes.couteau.graph.binding.BindingType;
import com.agileapes.couteau.graph.binding.impl.api.Query;

import java.util.Date;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/3/7 AD, 19:30)
 */
public class Simple {

    @Query(value = "[name]", type = BindingType.ATTRIBUTE, target = "name")
    private String name;
    @Query(value = "[id]", type = BindingType.ATTRIBUTE, target = "id")
    private Long id;
    @Query(value = "[date]", type = BindingType.ATTRIBUTE, target = "date")
    private Date date;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

}
