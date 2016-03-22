package org.kirill.todo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.kirill.todo.controller.ToDoController;
import org.kirill.todo.db.ToDoDBHandler;

/**
 * Created by kirill on 21.02.16.
 */
public class ToDoApplication {

    public static void main(String[] args) {
        ToDoDBHandler DBHandler = new ToDoDBHandler();
        System.out.println(PortResolver.getPort());
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        // CORS enabling
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PATCH)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));

        router.route().handler(BodyHandler.create());

        // to avoid writing it in every handler
        router.route("/").handler(ctx -> {
            ctx.response().putHeader("content-type", "application/json");
            ctx.next();
        });

        router.options("/").handler(ToDoController::options);

        router.get("/").handler(ToDoController::getAll);
        router.get("/:id").handler(ToDoController::getToDoById);

        router.patch("/:id").handler(ToDoController::modifyToDo);

        router.post("/").handler(ToDoController::postToDo);

        router.delete("/").handler(ToDoController::clearAll);
        router.delete("/:id").handler(ToDoController::deleteToDo);

        server.requestHandler(router::accept)
                .listen(PortResolver.getPort(), "0.0.0.0");
    }

}
