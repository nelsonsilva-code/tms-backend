package com.nelson.tms.service.impl;

import com.nelson.tms.dto.TodoDto;
import com.nelson.tms.entity.Todo;
import com.nelson.tms.exception.TodoNotFoundException;
import com.nelson.tms.repository.TodoRepository;
import com.nelson.tms.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService  {

    private TodoRepository todoRepository;

    private ModelMapper modelMapper;

    public void createTodo(TodoDto todoDto) {

        Todo todo = modelMapper.map(todoDto, Todo.class);

        todoRepository.save(todo);

    }

    public List<TodoDto> getAllTodo() {

        List<Todo> todos = todoRepository.findAll();

        return todos.stream()
                .map((todo -> modelMapper.map(todo, TodoDto.class)))
                .collect(Collectors.toList());
    }

    public TodoDto getTodoById(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException());

        return modelMapper.map(todo, TodoDto.class);

    }

    public void updateTodo(Long id, TodoDto todoDto) {

        Todo todo = modelMapper.map(this.getTodoById(id), Todo.class);

        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setComplete(todoDto.isComplete());

        todoRepository.save(todo);

        modelMapper.map(todo, TodoDto.class);
    }

    public void deleteTodo(Long id) {

        Todo todo = modelMapper.map(this.getTodoById(id), Todo.class);

        todoRepository.delete(todo);
    }

    public void completeTodo(Long id) {
        Todo todo = modelMapper.map(this.getTodoById(id), Todo.class);
        todo.setComplete(true);

        todoRepository.save(todo);
    }

    public void incompleteTodo(Long id) {
        Todo todo = modelMapper.map(this.getTodoById(id), Todo.class);
        todo.setComplete(false);

        todoRepository.save(todo);
    }
}
