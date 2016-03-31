package org.kirill.todo.model.mapper;

import io.vertx.core.json.JsonObject;
import org.kirill.todo.ToDoApplication;
import org.kirill.todo.model.ToDo;


/**
 * Created by kirill on 22.03.2016.
 *
 * Mapping Json objects to new To Do instance
 */
public class JsonToToDoMapper {

    public static ToDo convert (JsonObject jsonObject) {
        checkTitle(jsonObject);
        ToDo newToDo = new ToDo(jsonObject.getString("title"));
        newToDo.setOrder(getOrder(jsonObject));
        if (jsonObject.containsKey("completed")) {
            newToDo.setCompleted(jsonObject.getBoolean("completed"));
        }
        if (jsonObject.containsKey("order")) {
            newToDo.setOrder(jsonObject.getInteger("order"));
        }
        if (jsonObject.containsKey("id")) {
            int id = jsonObject.getInteger("id");
            newToDo.setId(id);
            if (ToDoApplication.getCurrentUrl() != null) {
                newToDo.setUrl(ToDoApplication.getCurrentUrl() + id);
            }
        }
        return newToDo;
    }

    private static void checkTitle(JsonObject jsonObject) {
        if (!jsonObject.containsKey("title")) {
            throw new IllegalArgumentException("new todo item must contain title");
        }
    }

    private static int getOrder(JsonObject jsonObject) {
        if (jsonObject.containsKey("order")) {
            return jsonObject.getInteger("order");
        } else {
            return 0;
        }
    }
}
