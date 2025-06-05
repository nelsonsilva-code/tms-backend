package com.nelson.tms.controller;

import com.nelson.tms.dto.TodoDto;
import com.nelson.tms.service.TodoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/todo")
@AllArgsConstructor
public class TodoController {

    private TodoService todoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Void> createTodo(@Valid @RequestBody TodoDto todoDto) {

        todoService.createTodo(todoDto);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public ResponseEntity<List> getAllTodo() {
        List<TodoDto> todoDto = todoService.getAllTodo();

        return ResponseEntity.ok(todoDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {

        TodoDto todoDto = todoService.getTodoById(id);

        return ResponseEntity.ok(todoDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}/update")
    public ResponseEntity<Void> updateTodo(@PathVariable Long id, @RequestBody TodoDto todoDto) {

        todoService.updateTodo(id, todoDto);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("{id}/complete")
    public ResponseEntity<Void> completeTodo(@PathVariable Long id) {
        todoService.completeTodo(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("{id}/incomplete")
    public ResponseEntity<Void> incompleteTodo(@PathVariable Long id) {
        todoService.incompleteTodo(id);

        return ResponseEntity.ok().build();
    }
}
