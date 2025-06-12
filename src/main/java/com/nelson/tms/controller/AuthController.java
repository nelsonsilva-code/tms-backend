package com.nelson.tms.controller;


import com.nelson.tms.dto.*;
import com.nelson.tms.entity.Permission;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/users/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        authService.createUser(createUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        HttpStatus httpStatus = authService.delete(id);
        return ResponseEntity.status(httpStatus).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','USER')")
    @PatchMapping("/users/{id}/update-password")
    public ResponseEntity<HttpStatus> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto, @PathVariable Long id){
        HttpStatus httpStatus = authService.updatePassword(updatePasswordDto, id);
        return ResponseEntity.status(httpStatus).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/roles")
    public ResponseEntity<List> getRoles() {
        List<Role> roles = authService.getRoles();

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/roles/create")
    public ResponseEntity<Role> createRole(@Valid @RequestBody RoleDto roleDto) {
        System.out.println(roleDto);
        Role role = authService.createRole(roleDto);

        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/permissions")
    public ResponseEntity<Permission[]> getPermissionList() {
        Permission[] permissions = authService.getPermissionList();

        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/users")
    public ResponseEntity<List> getUsers() {
        List<User> users = authService.getUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
