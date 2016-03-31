package org.kirill.todo.db;

import io.vertx.core.json.JsonObject;

import java.util.StringJoiner;

/**
 * Created by kirill on 22.03.2016.
 *
 * String composition can be rather complex,
 * so update is taken out here for clarity
 */
public class ToDoSQLComposer {
    public static String ComposeUpdateString(int id, JsonObject newParams) {
        StringBuilder sb = new StringBuilder("UPDATE Todo SET ");
        // join new values using new Java 8 API
        StringJoiner params = new StringJoiner(", ");
        if (newParams.containsKey("title")) {
            params.add("title='" + newParams.getString("title") + "'");
        }
        if (newParams.containsKey("completed")) {
            params.add("completed=" + newParams.getBoolean("completed"));
        }
        if (newParams.containsKey("order")) {
            params.add("orderx=" + newParams.getInteger("order"));
        }
        if (params.length() == 0) {
            throw new IllegalArgumentException("no info for update is provided");
        }
        sb.append(params.toString()).append(
            " WHERE id = " + id + ";"
        );
        return sb.toString();
    }
}
