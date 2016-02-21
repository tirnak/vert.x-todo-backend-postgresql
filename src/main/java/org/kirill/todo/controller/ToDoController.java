package org.kirill.todo.controller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.kirill.todo.model.ToDo;
import org.kirill.todo.model.ToDoCollection;

/**
 * Created by kirill on 21.02.16.
 */
public class ToDoController {

    private static ToDoCollection toDoCollection = new ToDoCollection();

    public static void options(RoutingContext ctx) {
        ctx.response().end();
    }

    public static void getAll(RoutingContext ctx) {
        ctx.response().end(toDoCollection.toString());
    }

    public static void getToDoById(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        ctx.response().end(toDoCollection.find(id).toString());
    }

    public static void postToDo(RoutingContext ctx) {
        JsonObject newToDoJson = ctx.getBodyAsJson();
        String currentUrl = ctx.request().absoluteURI();
        ToDo newToDo = toDoCollection.add(newToDoJson, currentUrl);
        ctx.response().end(newToDo.toString());
    }

    public static void modifyToDo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        JsonObject modifications = ctx.getBodyAsJson();
        ToDo toDoToModify = toDoCollection.find(id);
        toDoToModify.modify(modifications);
        ctx.response().end(toDoToModify.toString());
    }

    public static void deleteToDo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        toDoCollection.remove(id);
        getAll(ctx);
    }

    public static void clearAll(RoutingContext ctx) {
        toDoCollection.clear();
        getAll(ctx);
    }


}
