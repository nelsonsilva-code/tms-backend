package com.nelson.tms.controller;

import com.nelson.tms.dto.TodoDto;
import com.nelson.tms.service.TodoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/todo")
@AllArgsConstructor
public class TodoController {

    private TodoService todoService;

    @PostMapping("/create")
    public ResponseEntity<Void> createTodo(@Valid @RequestBody TodoDto todoDto) {

        todoService.createTodo(todoDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List> getAllTodo() {
        List<TodoDto> todoDto = todoService.getAllTodo();

        return ResponseEntity.ok(todoDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {

        TodoDto todoDto = todoService.getTodoById(id);

        return ResponseEntity.ok(todoDto);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<Void> updateTodo(@PathVariable Long id, @RequestBody TodoDto todoDto) {

        todoService.updateTodo(id, todoDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}/complete")
    public ResponseEntity<Void> completeTodo(@PathVariable Long id) {
        todoService.completeTodo(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}/incomplete")
    public ResponseEntity<Void> incompleteTodo(@PathVariable Long id) {
        todoService.incompleteTodo(id);

        return ResponseEntity.ok().build();
    }
}
