package org.kirill.todo.model;

import io.vertx.core.json.JsonObject;
import org.kirill.todo.controller.ToDoController;

import static org.kirill.todo.controller.ToDoController.*;

/**
 * Created by kise0116 on 22.03.2016.
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
            if (currentUrl != null) {
                newToDo.setUrl(currentUrl + id);
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
