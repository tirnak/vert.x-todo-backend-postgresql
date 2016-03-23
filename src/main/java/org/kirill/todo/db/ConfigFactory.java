package org.kirill.todo.db;

import io.vertx.core.json.JsonObject;
import org.kirill.todo.Function4args;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by kirill on 18.03.16.
 *
 * Factory returns configuration for DB.
 * you can write local.properties file similar to test.properties
 * on local machine in the same folder
 */
public class ConfigFactory {
    public static JsonObject getConfig() {

        /**
         * Let's use some Java 8 functional interfaces and lambdas.
         * Just because we can. =)
         */
        Function4args<String, JsonObject> fill = (host,  username,  password,  database) -> new JsonObject()
                .put("host", host).put("username", username)
                .put("password", password).put("database", database).put("sslmode", "require");

        /**
         *  First, try to get environment variable heroku.
         *  "heroku" can be set within heroku project directory by
         *  $ heroku config:set heroku=your_host\;your_username\;your_password\;name_of_database
         */
        String concatenated = System.getenv("heroku");
        if (concatenated != null) {
            String[] split = concatenated.split(";");
            return fill.apply(split[0], split[1], split[2], split[3]);
        }

        // then, try to find properties file
        ResourceBundle bundle = null;

        try {
            bundle = ResourceBundle.getBundle("local");
        } catch (MissingResourceException ignored) {
            System.out.println("local.properties not found");
            try {
                bundle = ResourceBundle.getBundle("test");
            } catch (MissingResourceException ignored2) {
                System.out.println("test.properties not found");
            }
        }

        /**
         * if local.properties file is found - use its values
         * else - use test.properties
         */
        if (bundle != null) {
            return fill.apply(
                    bundle.getString("host"), bundle.getString("username"),
                    bundle.getString("password"), bundle.getString("database"));
        } else {
            throw new RuntimeException("Something went wring during getting the config");
        }
    }
}
