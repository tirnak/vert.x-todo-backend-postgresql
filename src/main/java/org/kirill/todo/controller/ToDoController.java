package org.kirill.todo.controller;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.kirill.todo.ToDoApplication;
import org.kirill.todo.db.ToDoDBHandler;
import org.kirill.todo.model.ToDo;
import org.kirill.todo.model.mapper.JsonToToDoMapper;
import org.kirill.todo.model.mapper.ResultSetToToDoMapper;

/**
 * Created by kirill on 21.02.16.
 *
 * To Do controller handles all HTTP requests.
 * Mostly, by interacting with DB by means of ToDoDBHandler and
 * Mapper classes
 *
 */
public class ToDoController {

    /**
     * Just stub to pass backend tests
     */
    public static void options(RoutingContext ctx) {
        ctx.response().end();
    }

    public static void getAll(RoutingContext ctx) {
        ToDoDBHandler.selectAll(resultSetAsyncResult -> {
            JsonArray toDoList = ResultSetToToDoMapper.convertToJsonArray(resultSetAsyncResult.result());
            ctx.response().end(toDoList.toString());
        });

    }

    public static void getToDoById(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        ToDoDBHandler.select(id, resultSetAsyncResult -> {
            ToDo found = ResultSetToToDoMapper.convertFirst(resultSetAsyncResult.result()).get();
            ctx.response().end(found.toString());
        });

    }

    public static void postToDo(RoutingContext ctx) {
        JsonObject newToDoJson = ctx.getBodyAsJson();
        ToDo newToDo = JsonToToDoMapper.convert(newToDoJson);
        ToDoDBHandler.insert(newToDo, ignoredResult -> {
            ToDoDBHandler.getLastInserted(res -> {
                int newId = res.result().getResults().get(0).getInteger(0);
                newToDo.setId(newId);
                newToDo.setUrl(ToDoApplication.getCurrentUrl() + newId);
                ctx.response().end(newToDo.toString());
            });
        });

    }

    public static void modifyToDo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        JsonObject modifications = ctx.getBodyAsJson();
        ToDoDBHandler.update(id, modifications, updateResultAsyncResult -> {
            ToDoDBHandler.select(id, resultSetAsyncResult -> {
                ToDo changed = ResultSetToToDoMapper.convertFirst(resultSetAsyncResult.result()).get();
                ctx.response().end(changed.toString());
            });
        });
    }

    public static void deleteToDo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.request().getParam("id"));
        ToDoDBHandler.delete(id, ignoredParam -> getAll(ctx));
    }

    public static void clearAll(RoutingContext ctx) {
        ToDoDBHandler.deleteAll(ignoredParam -> getAll(ctx));
    }


}
