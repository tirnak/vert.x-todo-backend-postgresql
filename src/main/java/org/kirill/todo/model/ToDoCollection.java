package org.kirill.todo.model;

import io.vertx.core.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kirill on 21.02.16.
 */
public class ToDoCollection {
    private Map todos = new HashMap<Integer, ToDo>();

    public void clear() {
        todos.clear();
    }

    @Override
    public String toString() {
        return Json.encode(new ArrayList<>(todos.values()));
    }
}
