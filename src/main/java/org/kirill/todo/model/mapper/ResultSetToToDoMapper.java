package org.kirill.todo.model.mapper;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import org.kirill.todo.ToDoApplication;
import org.kirill.todo.model.ToDo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kirill on 22.03.2016.
 *
 * We need to handle select statements result
 */
public class ResultSetToToDoMapper {

    /**
     * table structure reminder
     *
     *   CREATE TABLE IF NOT EXISTS Todo(
     *   id          SERIAL  PRIMARY KEY,
     *   title       TEXT    NOT NULL,
     *   completed   BOOLEAN NOT NULL,
     *   orderx      INTEGER,
     */


    public static List<ToDo> convertToList (ResultSet resultSet) {
        return iterate(resultSet);
    }

    public static JsonArray convertToJsonArray (ResultSet resultSet) {
        JsonArray toDoList = new JsonArray();
        iterate(resultSet).forEach(toDo -> toDoList.add(toDo.toJsonObject()));
        return toDoList;
    }

    public static Optional<ToDo> convertFirst (ResultSet resultSet) {
        List<ToDo> list = iterate(resultSet);
        if (list.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
    }

    /**
     * Method, that handles converting itself.
     * Returns stream so that following operations can be lazily evaluated
     */
    private static List<ToDo> iterate(ResultSet results) {
        List<JsonArray> tmpResults = results.getResults();
        if (tmpResults.size() == 0) {
            return Collections.<ToDo>emptyList();
        }
        return tmpResults.stream().map(row -> {
            ToDo tempToDo = new ToDo(   row.getString(1));
            tempToDo.setId(             row.getInteger(0));
            tempToDo.setCompleted(      row.getBoolean(2));
            tempToDo.setOrder(          row.getInteger(3));
            if (ToDoApplication.getCurrentUrl() != null) {
                tempToDo.setUrl(ToDoApplication.getCurrentUrl() + row.getInteger(0));
            }
            return tempToDo;
        }).collect(Collectors.toList());
    }


}
