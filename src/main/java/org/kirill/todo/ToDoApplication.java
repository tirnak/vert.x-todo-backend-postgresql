    package org.kirill.todo;

    import io.vertx.core.Vertx;
    import io.vertx.core.http.HttpMethod;
    import io.vertx.core.http.HttpServer;
    import io.vertx.core.http.HttpServerResponse;
    import io.vertx.core.json.Json;
    import io.vertx.core.json.JsonObject;
    import io.vertx.ext.web.Router;
    import io.vertx.ext.web.handler.BodyHandler;
    import io.vertx.ext.web.handler.CorsHandler;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;

/**ls
 * Created by kirill on 18.02.16.
 */
public class ToDoApplication {

    private static Map<Integer, Activity> todos = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(PortResolver.getPort());
        // mock
        fillTodos();
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));
        router.route().handler(BodyHandler.create());


        router.options("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            response.end(Json.encode(new ArrayList<>(todos.values())));
        });
        router.get("/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            response.end(Json.encode(new ArrayList<>(todos.values())));
        });
        router.post("/").handler(routingContext -> {
            JsonObject task = routingContext.getBodyAsJson();
            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");

            response.end(task.encode());
        });

        server.requestHandler(router::accept)
                .listen(PortResolver.getPort(), "0.0.0.0");
    }

    private static void fillTodos() {
        todos.put(1, new Activity(1, "Hell, yeah!", false, 1));
        todos.put(2, new Activity(2, "Hell, yeah!", false, 3));
        todos.put(3, new Activity(3, "Hell, yeah!", false, 1));
    }

}
