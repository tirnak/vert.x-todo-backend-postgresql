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
                .put("password", password).put("database", database);

        // first, initialize test values
        ResourceBundle bundle = ResourceBundle.getBundle("test");

        try {
            // then, try to load DB configuration from local.properties
            bundle = ResourceBundle.getBundle("local");
        } catch (MissingResourceException ignored) {
            /**
             *  If local.properties file is not found, try to get environment variable heroku
             *  "heroku" can be set within heroku project directory by
             *  $ heroku config:set heroku=your_host\;your_username\;your_password\;name_of_database
             */
            String concatenated = System.getenv("heroku");
            if (concatenated != null) {
                String[] split = concatenated.split(";");
                return fill.apply(split[0], split[1], split[2], split[3]);
            }
        } finally {
            /**
             * if local.properties file is found - use its values
             * else - use test.properties
             */
            return fill.apply(
                    bundle.getString("host"), bundle.getString("username"),
                    bundle.getString("password"), bundle.getString("database"));
        }
    }
}
