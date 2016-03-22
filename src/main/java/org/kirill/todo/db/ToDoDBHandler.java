package org.kirill.todo.db;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import org.kirill.todo.db.ConfigFactory;
import org.kirill.todo.model.ToDo;

import java.sql.SQLException;

/**
 * Created by kirill on 17.03.16.
 */
public class ToDoDBHandler {
    private static AsyncSQLClient client;

    static {
        JsonObject postgreSQLClientConfig = ConfigFactory.getConfig();
        client = PostgreSQLClient.createShared(Vertx.vertx(), postgreSQLClientConfig);
        client.getConnection(res -> {
            if (! res.succeeded()) {
                System.out.println(res.cause());
                throw new RuntimeException(res.cause());
            }
            res.result().execute(
                    "CREATE TABLE IF NOT EXISTS Todo(" +
                    "   id          SERIAL  PRIMARY KEY," +
                    "   title       TEXT    NOT NULL," +
                    "   completed   BOOLEAN NOT NULL," +
                    "   orderx       INTEGER," +
                    "   url         TEXT" +
                    ");", res2 -> {
                        if (! res2.succeeded()) {
                            System.out.println(res2.cause());
                            throw new RuntimeException(res2.cause());
                        }
                    });
        });
    }

    public static void insert(ToDo toDo, Handler<AsyncResult> next) throws SQLException {
        client.getConnection(connRes -> connRes.result()
            .execute("INSERT INTO Todo VALUES" +
                "('" + toDo.getTitle() + "', " + toDo.getCompleted()
                + ", " + toDo.getOrder() + ", '" + toDo.getUrl() + "');",
                res -> next.handle(res))
        );
    }

    public static void update(ToDo toDo) throws SQLException {
        client.getConnection(res -> res.result()
            .execute("UPDATE Todo SET title='" +
            toDo.getTitle() + "', completed=" + toDo.getCompleted()
            + ", order=" + toDo.getOrder() + " WHERE id = " + toDo.getId() + ";",
            ignored -> {})
        );
    }

    public static void delete(int id) throws SQLException {
        client.getConnection(res -> res.result()
            .execute("DELETE FROM Todo WHERE id = " + id + ";",
            ignored -> {})
        );
    }

    public static void deleteAll() throws SQLException {
        client.getConnection(res -> res.result()
            .execute("DELETE FROM Todo;",
            ignored -> {})
        );
    }


}
