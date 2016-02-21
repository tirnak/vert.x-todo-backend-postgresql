package org.kirill.todo.model;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Some locking is required in order to get consistent state
 * Created by kirill on 21.02.16.
 */
public class ToDoCollection {
    private AtomicInteger counter = new AtomicInteger();
    private Map<Integer, ToDo> todos = new HashMap<>();
    private Lock readLock;
    private Lock writeLock;
    {
        // less blocking, then synchronised
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public void clear() {
        writeLock.lock();
        todos.clear();
        writeLock.unlock();
    }

    public ToDo add(JsonObject jsonObject, String url) {
        writeLock.lock();
        checkTitle(jsonObject);
        ToDo newToDo = new ToDo(jsonObject.getString("title"));
        int index = counter.incrementAndGet();
        newToDo.setId(index);
        newToDo.setUrl(url + index);
        newToDo.setOrder(getOrder(jsonObject));
        todos.put(index, newToDo);
        writeLock.unlock();
        return newToDo;
    }

    private void checkTitle(JsonObject jsonObject) {
        if (!jsonObject.containsKey("title")) {
            throw new IllegalArgumentException("new todo item must contain title");
        }
    }

    private int getOrder(JsonObject jsonObject) {
        if (jsonObject.containsKey("order")) {
            return jsonObject.getInteger("order");
        } else {
            return 0;
        }
    }



    public ToDo find(int index) {
        return todos.get(index);
    }

    public void remove(int index) {
        todos.remove(index);
    }

    @Override
    public String toString() {
        readLock.lock();
        String toReturn = Json.encode(new ArrayList<>(todos.values()));
        readLock.unlock();
        return toReturn;
    }
}
