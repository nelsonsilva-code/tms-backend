package com.nelson.tms.service.impl;

import com.nelson.tms.dto.TodoDto;
import com.nelson.tms.entity.Todo;
import com.nelson.tms.exception.ResourceNotFoundException;
import com.nelson.tms.repository.TodoRepository;
import com.nelson.tms.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        long start = System.currentTimeMillis();

        List<Todo> todos = todoRepository.findAll();


        List<TodoDto> todosList =  todos.stream()
                .map((todo -> modelMapper.map(todo, TodoDto.class)))
                .collect(Collectors.toList());

        System.out.println("Time to fetch and map todos: " + (System.currentTimeMillis() - start) + "ms");

        return todosList;
    }

    public TodoDto getTodoById(Long id) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with given ID does not exist."));

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
