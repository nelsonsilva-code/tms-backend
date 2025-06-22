package com.nelson.tms.controller;

import com.nelson.tms.dto.CreateUserDto;
import com.nelson.tms.dto.UpdatePasswordDto;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping()
    public ResponseEntity<List> getUsers() {
        List<User> users = userService.getUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        HttpStatus httpStatus = userService.delete(id);
        return ResponseEntity.status(httpStatus).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','USER')")
    @PatchMapping("/{id}/update-password")
    public ResponseEntity<HttpStatus> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto, @PathVariable Long id){
        HttpStatus httpStatus = userService.updatePassword(updatePasswordDto, id);
        return ResponseEntity.status(httpStatus).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/update-role")
    public ResponseEntity<HttpStatus> updateRole(@RequestBody String newRole, @PathVariable Long id){
        logger.info("Called");
        HttpStatus httpStatus = userService.updateRole(newRole, id);
        return ResponseEntity.status(httpStatus).build();
    }


}
