package org.kirill.todo.model;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import org.kirill.todo.controller.ToDoController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.kirill.todo.controller.ToDoController.currentUrl;

/**
 * Created by kise0116 on 22.03.2016.
 */
public class ResultSetToToDoMapper {

    /**
     * DB reminder
     *
     *   CREATE TABLE IF NOT EXISTS Todo(
     *   id          SERIAL  PRIMARY KEY,
     *   title       TEXT    NOT NULL,
     *   completed   BOOLEAN NOT NULL,
     *   orderx      INTEGER,
     */

    public static List<ToDo> convertToList (ResultSet resultSet) {
        List<JsonArray> results = resultSet.getResults();
        List<ToDo> toDoList = new ArrayList<>();
        iterate(results, toDoList::add);
        return toDoList;
    }

    public static JsonArray convertToJsonArray (ResultSet resultSet) {
        List<JsonArray> results = resultSet.getResults();
        JsonArray toDoList = new JsonArray();
        iterate(results, toDoList::add);
        return toDoList;
    }

    private static void iterate(List<JsonArray> results,  Consumer<ToDo> command) {
        for (JsonArray row: results) {
            ToDo tempToDo = new ToDo(   row.getString(1));
            tempToDo.setId(             row.getInteger(0));
            tempToDo.setCompleted(      row.getBoolean(3));
            tempToDo.setOrder(          row.getInteger(3));
            if (currentUrl != null) {
                tempToDo.setUrl(currentUrl + row.getInteger(0));
            }
            command.accept(tempToDo);
        }

    }


}
