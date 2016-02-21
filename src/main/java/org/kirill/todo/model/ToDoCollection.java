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
 * Created by kirill on 21.02.16.
 */
public class ToDoCollection {
    private AtomicInteger counter = new AtomicInteger();
    private Map todos = new HashMap<Integer, ToDo>();
    private Lock readLock;
    private Lock writeLock;
    {
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
        if (!jsonObject.containsKey("title")) {
            throw new IllegalArgumentException("new todo item must contain title");
        }
        ToDo newToDo = new ToDo(jsonObject.getString("title"));
        int index = counter.incrementAndGet();
        newToDo.setId(index);
        newToDo.setUrl(url+index);
        if (jsonObject.containsKey("order")) {
            newToDo.setOrder(jsonObject.getInteger("order"));
        }
        todos.put(index, newToDo);
        writeLock.unlock();
        return newToDo;
    }

    public ToDo find(int index) {
        return (ToDo) todos.get(index);
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
