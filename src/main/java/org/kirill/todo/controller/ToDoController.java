package org.kirill.todo.controller;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.RoutingContext;
import org.kirill.todo.model.mapper.JsonToToDoMapper;
import org.kirill.todo.db.ToDoDBHandler;
import org.kirill.todo.model.mapper.ResultSetToToDoMapper;
import org.kirill.todo.model.ToDo;

/**
 * Created by kirill on 21.02.16.
 *
 * To Do controller handles all HTTP requests.
 * Mostly, by interacting with DB by means of ToDoDBHandler and
 * Mapper classes
 *
 */
public class ToDoController {

    public static String currentUrl;

    /**
     * Just stub to pass backend tests
     * @param ctx
     */
    public static void options(RoutingContext ctx) {
        ctx.response().end();
    }

    public static void getAll(RoutingContext ctx) {
        currentUrl = ctx.request().absoluteURI();
        ToDoDBHandler.selectAll(resultSetAsyncResult -> {
            JsonArray toDoList = ResultSetToToDoMapper.convertToJsonArray(resultSetAsyncResult.result());
            ctx.response().end(toDoList.toString());
        });

    }

    public static void getToDoById(RoutingContext ctx) {
        currentUrl = ctx.request().absoluteURI();
        int id = Integer.parseInt(ctx.request().getParam("id"));
        ToDoDBHandler.select(id, resultSetAsyncResult -> {
            ToDo found = ResultSetToToDoMapper.convertToList(resultSetAsyncResult.result()).get(0);
            ctx.response().end(found.toString());
        });

    }

    public static void postToDo(RoutingContext ctx) {
        currentUrl = ctx.request().absoluteURI();
        JsonObject newToDoJson = ctx.getBodyAsJson();
        ToDo newToDo = JsonToToDoMapper.convert(newToDoJson);
        ToDoDBHandler.insert(newToDo, ignoredResult -> ToDoDBHandler.getLastInserted(res -> {
            int newId = res.result().getResults().get(0).getInteger(0);
            newToDo.setId(newId);
            newToDo.setUrl(currentUrl + newId);
            ctx.response().end(newToDo.toString());
        }));

    }

    public static void modifyToDo(RoutingContext ctx) {
        currentUrl = ctx.request().absoluteURI();
        int id = Integer.parseInt(ctx.request().getParam("id"));
        JsonObject modifications = ctx.getBodyAsJson();
        ToDoDBHandler.update(id, modifications, updateResultAsyncResult -> {
            ToDoDBHandler.select(id, resultSetAsyncResult -> {
                ToDo changed = ResultSetToToDoMapper.convertFirst(resultSetAsyncResult.result());
                ctx.response().end(changed.toString());
            });
        });
    }

    public static void deleteToDo(RoutingContext ctx) {
        currentUrl = ctx.request().absoluteURI();
        int id = Integer.parseInt(ctx.request().getParam("id"));
        ToDoDBHandler.delete(id, ignoredParam -> getAll(ctx));
    }

    public static void clearAll(RoutingContext ctx) {
        currentUrl = ctx.request().absoluteURI();
        ToDoDBHandler.deleteAll(ignoredParam -> getAll(ctx));
    }


}
