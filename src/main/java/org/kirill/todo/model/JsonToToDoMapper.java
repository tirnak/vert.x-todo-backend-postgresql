package org.kirill.todo.model;

import io.vertx.core.json.JsonObject;

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
