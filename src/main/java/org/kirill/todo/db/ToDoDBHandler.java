package org.kirill.todo.db;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.UpdateResult;
import org.kirill.todo.model.ToDo;
import io.vertx.ext.sql.ResultSet;

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
                    ");", res2 -> {
                        if (! res2.succeeded()) {
                            System.out.println(res2.cause());
                            throw new RuntimeException(res2.cause());
                        }
                    });
        });
    }

    public static void insert(ToDo toDo, Handler<AsyncResult<UpdateResult>> next) {
        client.getConnection(connRes -> connRes.result()
            .update("INSERT INTO Todo VALUES" +
                            "('" + toDo.getTitle() + "', " + toDo.getCompleted()
                            + ", " + toDo.getOrder() + "');",
                    res -> next.handle(res))
        );
    }

    public static void selectAll(Handler<AsyncResult<ResultSet>> next) {
        client.getConnection(res -> res.result()
            .execute("SELECT * FROM Todo",
            ignored -> {})
        );
    }

    public static void select(int id, Handler<AsyncResult<ResultSet>> next) {
        client.getConnection(res -> res.result()
            .execute("SELECT * FROM Todo WHERE id = " + id + ";",
            ignored -> {})
        );
    }

    public static void update(int id, JsonObject newParams, Handler<AsyncResult<UpdateResult>> next) {
        client.getConnection(res -> res.result()
            .update(ToDoSQLComposer.ComposeUpdateString(id, newParams), next::handle)
        );
    }

    public static void delete(int id, Handler<AsyncResult<UpdateResult>> next) {
        client.getConnection(res -> res.result()
            .update("DELETE FROM Todo WHERE id = " + id + ";", next::handle)
        );
    }

    public static void deleteAll(Handler<AsyncResult<UpdateResult>> next) {
        client.getConnection(res -> res.result()
            .update("DELETE FROM Todo;", next::handle)
        );
    }


}
