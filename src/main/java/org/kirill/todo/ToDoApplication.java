    package org.kirill.todo;

    import io.vertx.core.Vertx;
    import io.vertx.core.http.HttpMethod;
    import io.vertx.core.http.HttpServer;
    import io.vertx.core.http.HttpServerResponse;
    import io.vertx.core.json.JsonObject;
    import io.vertx.ext.web.Router;
    import io.vertx.ext.web.handler.BodyHandler;
    import io.vertx.ext.web.handler.CorsHandler;
    import org.kirill.todo.controller.ToDoController;
    import org.kirill.todo.model.ToDo;

    import java.util.HashMap;
    import java.util.Map;

/**ls
 * Created by kirill on 18.02.16.
 */
public class ToDoApplication {

    public static void main(String[] args) {
        System.out.println(PortResolver.getPort());
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PATCH)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));
        router.route().handler(BodyHandler.create());


        router.options("/").handler(ToDoController::options);
        router.get("/").handler(ToDoController::getAll);
        router.get("/:id").handler(ToDoController::getById);
        router.post("/").handler(ToDoController::postToDo);
        router.delete("/").handler(ToDoController::clear);

        server.requestHandler(router::accept)
                .listen(PortResolver.getPort(), "0.0.0.0");
    }

}
