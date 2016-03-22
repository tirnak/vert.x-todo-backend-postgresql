package org.kirill.todo.db;

import io.vertx.core.json.JsonObject;

import java.util.ResourceBundle;

/**
 * Created by kirill on 18.03.16.
 */
public class ConfigFactory {
    public static JsonObject getConfig() {
        ResourceBundle bundle = ResourceBundle.getBundle("local");
        if (bundle == null) {
            String concatenated = System.getenv("heroku");
            if (concatenated != null) {
                String[] split = concatenated.split(";");
                return new JsonObject()
                    .put("host", split[0])
                    .put("username", split[1])
                    .put("password", split[2])
                    .put("database", split[3]);
            }
        }
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("test");
        }
        return new JsonObject()
                .put("host", bundle.getString("host"))
                .put("username", bundle.getString("username"))
                .put("password", bundle.getString("password"))
                .put("database", bundle.getString("database"));
    }
}
