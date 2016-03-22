package org.kirill.todo.db;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import org.kirill.todo.model.ToDo;
import io.vertx.ext.sql.ResultSet;

/**
 * Created by kirill on 17.03.16.
 * Here static methods are used,
 * because no advanced architecture is needed for To Do app
 *
 * URL is not stored, because it can be derived
 * from request hostname and To Do's id in runtime
 */
public class ToDoDBHandler {
    private static AsyncSQLClient client;

    /**
     * Initialize client
     * No exception handling,
     * since without proper DB connection app wouldn't work properly
     */
    static {
        JsonObject postgreSQLClientConfig = ConfigFactory.getConfig();
        client = PostgreSQLClient.createShared(Vertx.vertx(), postgreSQLClientConfig);
        client.getConnection(res -> {
            if (! res.succeeded()) {
                System.out.println(res.cause());
                throw new RuntimeException(res.cause());
            }
            /**
             * Create To Do table, if there is none.
             * "orderx" because PostgreSQL wouldn't let name column as "order"
             */
            res.result().execute(
                    "CREATE TABLE IF NOT EXISTS Todo(" +
                    "   id          SERIAL  PRIMARY KEY," +
                    "   title       TEXT    NOT NULL," +
                    "   completed   BOOLEAN NOT NULL," +
                    "   orderx       INTEGER" +
                    ");", res2 -> {
                        if (! res2.succeeded()) {
                            System.out.println(res2.cause());
                            throw new RuntimeException(res2.cause());
                        }
                    });
        });
    }

    public static void insert(ToDo toDo, Handler<AsyncResult<UpdateResult>> next) {
        applyToConnection(res -> res.update(
            "INSERT INTO Todo (title, completed, orderx) VALUES"
            + "('" + toDo.getTitle() + "', " + toDo.isCompleted()
            + ", " + toDo.getOrder() + ") RETURNING id;", next::handle)
        );
    }

    public static void getLastInserted(Handler<AsyncResult<ResultSet>> next) {
        applyToConnection(res -> res.query(
            "SELECT last_value from todo_id_seq;", next::handle)
        );
    }

    public static void selectAll(Handler<AsyncResult<ResultSet>> next) {
        applyToConnection(
            res -> res.query("SELECT * FROM Todo", next::handle)
        );
    }

    public static void select(int id, Handler<AsyncResult<ResultSet>> next) {
        applyToConnection(
            res -> res.query("SELECT * FROM Todo WHERE id = " + id + ";", next::handle)
        );
    }


    public static void update(int id, JsonObject newParams, Handler<AsyncResult<UpdateResult>> next) {
        applyToConnection(
            res -> res.update(ToDoSQLComposer.ComposeUpdateString(id, newParams), next::handle)
        );
    }

    public static void delete(int id, Handler<AsyncResult<UpdateResult>> next) {
        applyToConnection(
            res -> res.update("DELETE FROM Todo WHERE id = " + id + ";", next::handle)
        );
    }

    public static void deleteAll(Handler<AsyncResult<UpdateResult>> next) {
        applyToConnection(res -> res.update("DELETE FROM Todo;", next::handle));
    }

    /**
     * This is common connection handling code
     * Here is the place to write your exceptions handling etc.
     */
    private static void applyToConnection(Handler<SQLConnection> action) {
        client.getConnection(asyncResult -> action.handle(asyncResult.result()));
    }


}
