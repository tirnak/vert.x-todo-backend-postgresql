package org.kirill.todo.controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.kirill.todo.model.ToDo;
import org.kirill.todo.model.ToDoCollection;

import java.util.ArrayList;

/**
 * Created by kirill on 21.02.16.
 */
public class ToDoController {

    private static ToDoCollection toDoCollection = new ToDoCollection();

    public static void options(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "application/json");
        response.end();
    }

    public static void getAll(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "application/json");
        response.end(toDoCollection.toString());
    }

    public static void getById(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "application/json");
        response.end(toDoCollection.find(id).toString());
    }

    public static void postToDo(RoutingContext ctx) {
        JsonObject newToDoJson = ctx.getBodyAsJson();
        // This handler will be called for every request
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "application/json");
        String currentUrl = ctx.request().absoluteURI();
        ToDo newToDo = toDoCollection.add(newToDoJson, currentUrl);
        response.end(newToDo.toString());
    }

    public static void modifyToDo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        JsonObject modifications = ctx.getBodyAsJson();
        ToDo toDoToModify = toDoCollection.find(id);
        toDoToModify.modify(modifications);
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "application/json");
        response.end(toDoToModify.toString());
    }

    public static void deleteToDo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        toDoCollection.remove(id);
        getAll(ctx);
    }

    public static void clear(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "application/json");
        toDoCollection.clear();
        getAll(ctx);
    }


}
