package org.kirill.todo.db;

import io.vertx.core.json.JsonObject;
import org.kirill.todo.Function4args;

import java.util.HashMap;
import java.util.Map;
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
         * 5432 seems to be default port everywhere - so no need in parsing it here. Change for production
         */
        Function4args<String, JsonObject> fill = (host,  username,  password,  database) -> new JsonObject()
                .put("host", host).put("username", username)
                .put("password", password).put("database", database)
                .put("sslmode", "require").put("port", 5432);

        /**
         *  First, try to get environment variable JDBC_DATABASE_URL.
         *  you can check up parameters with "heroku run echo \$JDBC_DATABASE_URL" command
         */
        String concatenated = System.getenv("JDBC_DATABASE_URL");
        if (concatenated != null) {
            Map<String, String> parsed = parseJDBCString(concatenated);
            return fill.apply(parsed.get("host"),parsed.get("username"),parsed.get("password"),
                    parsed.get("database"));
        }

        // if no heroku variable was found, look up properties file
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

    /**
     * just primitive parsing function here
     */
    private static Map<String,String> parseJDBCString(String url) {
        Map<String, String> toReturn = new HashMap<>();
        String firstSplit = url.split("jdbc:postgresql://")[1];
        String[] tmp = firstSplit.split(":");
        toReturn.put("host", tmp[0]);
        tmp = tmp[1].split("/");
        tmp = tmp[1].split("\\?");
        toReturn.put("database", tmp[0]);
        tmp = tmp[1].split("&");
        toReturn.put("username", tmp[0].split("user=")[1]);
        toReturn.put("password", tmp[1].split("password=")[1]);
        return toReturn;
    }
}
