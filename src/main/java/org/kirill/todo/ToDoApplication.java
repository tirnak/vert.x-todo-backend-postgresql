    package org.kirill.todo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

import java.util.*;

/**ls
 * Created by kirill on 18.02.16.
 */
public class ToDoApplication {

    private static Map<Integer, Activity> todos = new HashMap<>();

    public static void main(String[] args) {
        fillTodos();
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, "/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            addCorsHeaders(response);
            response.end(Json.encode(new ArrayList<>(todos.values())));
        });
        router.route(HttpMethod.POST, "/:name").handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            String name = routingContext.request().getParam("name");
            addCorsHeaders(response);
            response.end("Hello, " + name + ", from Vert.x-Web!");
        });

        server.requestHandler(router::accept)
                .listen(8080, "0.0.0.0");
    }

    private static void addCorsHeaders(HttpServerResponse response) {
        response.putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static void fillTodos() {
        todos.put(1, new Activity(1, "Hell, yeah!", false, 1));
        todos.put(2, new Activity(2, "Hell, yeah!", false, 3));
        todos.put(3, new Activity(3, "Hell, yeah!", false, 1));
    }
}

//
//private Set<Todo> todos = new HashSet<>();
//
//        @RequestMapping(method = GET)
//        public HttpEntity<Collection<ResourceWithUrl>> listAll() {
//        public HttpEntity<Collection<ResourceWithUrl>> listAll() {
//            List<ResourceWithUrl> resourceWithUrls = todos.stream().map(todo -> toResource(todo)).collect(Collectors.toList());
//            return new ResponseEntity<>(resourceWithUrls, OK);
//        }
//
//        @RequestMapping(value = "/{todo-id}", method = GET)
//        public HttpEntity<ResourceWithUrl> getTodo(@PathVariable("todo-id") long id) {
//
//            Optional<Todo> todoOptional = tryToFindById(id);
//
//            if (!todoOptional.isPresent())
//                return new ResponseEntity<>(NOT_FOUND);
//
//            return respondWithResource(todoOptional.get(), OK);
//        }
//
//        private Optional<Todo> tryToFindById(long id) {
//            return todos.stream().filter(todo -> todo.getId() == id).findFirst();
//        }
//
//        @RequestMapping(method = POST,  headers = {"Content-type=application/json"})
//        public HttpEntity<ResourceWithUrl> saveTodo(@RequestBody Todo todo) {
//            todo.setId(todos.size() + 1);
//            todos.add(todo);
//
//            return respondWithResource(todo, CREATED);
//        }
//
//        @RequestMapping(method = DELETE)
//        public void deleteAllTodos() {
//            todos.clear();
//        }
//
//        @RequestMapping(value = "/{todo-id}", method = DELETE)
//        public void deleteOneTodo(@PathVariable("todo-id") long id) {
//            Optional<Todo> todoOptional = tryToFindById(id);
//
//            if ( todoOptional.isPresent() ) {
//                todos.remove(todoOptional.get());
//            }
//        }
//
//        @RequestMapping(value = "/{todo-id}", method = PATCH, headers = {"Content-type=application/json"})
//        public HttpEntity<ResourceWithUrl> updateTodo(@PathVariable("todo-id") long id, @RequestBody Todo newTodo) {
//            Optional<Todo> todoOptional = tryToFindById(id);
//
//            if ( !todoOptional.isPresent() ) {
//                return new ResponseEntity<>(NOT_FOUND);
//            } else if ( newTodo == null ) {
//                return new ResponseEntity<>(BAD_REQUEST);
//            }
//
//            todos.remove(todoOptional.get());
//
//            Todo mergedTodo = todoOptional.get().merge(newTodo);
//            todos.add(mergedTodo);
//
//            return respondWithResource(mergedTodo, OK);
//        }
//
//
//        private String getHref(Todo todo) {
//            return linkTo(methodOn(this.getClass()).getTodo(todo.getId())).withSelfRel().getHref();
//        }
//
//        private ResourceWithUrl toResource(Todo todo) {
//            return new ResourceWithUrl(todo, getHref(todo));
//        }
//
//        private HttpEntity<ResourceWithUrl> respondWithResource(Todo todo, HttpStatus statusCode) {
//            ResourceWithUrl resourceWithUrl = toResource(todo);
//
//            return new ResponseEntity<>(resourceWithUrl, statusCode);
//        }