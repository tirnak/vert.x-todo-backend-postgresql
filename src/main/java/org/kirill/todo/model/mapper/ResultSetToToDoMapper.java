package org.kirill.todo.model.mapper;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import org.kirill.todo.controller.ToDoController;
import org.kirill.todo.model.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.kirill.todo.controller.ToDoController.currentUrl;

/**
 * Created by kise0116 on 22.03.2016.
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
        List<ToDo> toDoList = new ArrayList<>();
        iterate(resultSet).forEach(toDoList::add);
        return toDoList;
    }

    public static JsonArray convertToJsonArray (ResultSet resultSet) {
        JsonArray toDoList = new JsonArray();
        iterate(resultSet).forEach(toDo -> toDoList.add(toDo.toJsonObject()));
        return toDoList;
    }

    public static ToDo convertFirst (ResultSet resultSet) {
        Optional<ToDo> optional = iterate(resultSet).findFirst();
        return optional.get();
    }

    /**
     * Method, that handles converting itself.
     * Returns stream so that following operations can be lazily evaluated
     */
    private static Stream<ToDo> iterate(ResultSet results) {
        return results.getResults().stream().map(row -> {
            ToDo tempToDo = new ToDo(   row.getString(1));
            tempToDo.setId(             row.getInteger(0));
            tempToDo.setCompleted(      row.getBoolean(2));
            tempToDo.setOrder(          row.getInteger(3));
            if (currentUrl != null) {
                tempToDo.setUrl(currentUrl + row.getInteger(0));
            }
            return tempToDo;
        });
    }


}
