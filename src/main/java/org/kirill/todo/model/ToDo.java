package org.kirill.todo.model;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * Created by kirill on 21.02.16.
 */
public class ToDo {

    private int id;
    private String title;
    private boolean completed;
    private int order;
    private String url;

    public ToDo(String title) {
        this.title = title;
    }

    public void modify(JsonObject jsonObject) {
        if (jsonObject.containsKey("title")) {
            title = jsonObject.getString("title");
        }
        if (jsonObject.containsKey("completed")) {
            completed = jsonObject.getBoolean("completed");
        }
        if (jsonObject.containsKey("order")) {
            order = jsonObject.getInteger("order");
        }
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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }

}