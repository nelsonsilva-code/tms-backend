package com.nelson.tms.controller;


import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.RegisterDto;
import com.nelson.tms.service.AuthService;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody LoginDto loginDto) {
        HttpStatus httpStatus = authService.login(loginDto);
        return ResponseEntity.status(httpStatus).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        HttpStatus httpStatus = authService.delete(id);
        return ResponseEntity.status(httpStatus).build();
    }
}
