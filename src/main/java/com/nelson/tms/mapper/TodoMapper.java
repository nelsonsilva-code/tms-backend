package com.nelson.tms.mapper;

import com.nelson.tms.dto.TodoDto;
import com.nelson.tms.entity.Todo;

//Currently using ModelMapper
public class TodoMapper {
    public static TodoDto mapToTodoDto(Todo todo) {
        return new TodoDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isComplete()
        );
    }

    public static Todo mapToTodo(TodoDto todoDto) {
        return new Todo(
                todoDto.getId(),
                todoDto.getTitle(),
                todoDto.getDescription(),
                todoDto.isComplete()
        );
    }
}
