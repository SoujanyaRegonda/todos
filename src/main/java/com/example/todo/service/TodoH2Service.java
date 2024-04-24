
package com.example.todo.service;

import com.example.todo.repository.TodoRepository;

import com.example.todo.model.Todo;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.todo.model.TodoRowMapper;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class TodoH2Service implements TodoRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Todo> getTodos() {
        List<Todo> todoData = db.query("SELECT * FROM TODOLIST", new TodoRowMapper());
        ArrayList<Todo> todos = new ArrayList<>(todoData);
        return todos;
    }

    @Override
    public Todo getTodoById(int id) {
        try {
            Todo todo = db.queryForObject("SELECT * FROM TODOLIST WHERE id = ?", new TodoRowMapper(), id);
            return todo;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }

    }

    @Override
    public Todo addTodo(Todo todo) {
        db.update("INSERT INTO TODOLIST(todo, priority, status) values (?, ?, ?)", todo.getTodo(), todo.getPriority(), todo.getStatus());
        Todo savedTodo = db.queryForObject("SELECT * FROM TODOLIST WHERE todo = ?", new TodoRowMapper(), todo.getTodo());
        return savedTodo;
        

    }

    @Override
    public Todo updateTodo(int id, Todo todo) {
        if (todo.getTodo() != null) {
            db.update("UPDATE TODOLIST SET todo = ? WHERE id = ?", todo.getTodo(), id);

        }
        if (todo.getPriority() != null) {
            db.update("UPDATE TODOLIST SET priority = ? WHERE id = ?", todo.getPriority(), id);

        }
        if (todo.getStatus() != null) {
            db.update("UPDATE TODOLIST SET status = ? WHERE id = ?", todo.getStatus(), id);

        }
        return getTodoById(id);
        

    }

    @Override
    public void deleteTodo(int id) {
        db.update("DELETE FROM TODOLIST WHERE id = ?", id);

    }

}
