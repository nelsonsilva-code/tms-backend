package com.nelson.tms.service;

import com.nelson.tms.dto.TodoDto;

import java.util.List;
import java.util.Optional;

public interface TodoService {

    void createTodo(TodoDto todoDto);

    List<TodoDto> getAllTodo();

    TodoDto getTodoById(Long id);

    void updateTodo(Long id, TodoDto todoDto);

    void deleteTodo(Long id);

    void completeTodo(Long id);

    void incompleteTodo(Long id);
}
