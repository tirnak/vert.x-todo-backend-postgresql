package org.kirill.todo.model;

import io.vertx.core.json.Json;

import java.util.ArrayList;

/**
 * Created by kirill on 18.02.16.
 */
public class ToDo {


    private int id;
    private String title;
    private boolean completed;
    private int order;
    private String url;

    public ToDo() {}

    public ToDo(String title) {
        this.title = title;
    }

    public ToDo(int id, String title, Boolean completed, Integer order, String url) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDo todo = (ToDo) o;

        if (id != todo.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setIdAndUrl(int id) {
        this.id = id;
        this.url = id + "";
    }

    @Override
    public String toString() {
        return Json.encode(this);
    }

    /**
     * trivial getters/setters
     * promarily for JSONing
     */
    public boolean isCompleted() {
        return completed;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public int getId() {
        return id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}