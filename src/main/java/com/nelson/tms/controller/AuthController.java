package com.nelson.tms.controller;


import com.nelson.tms.dto.JwtAuthResponse;
import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.CreateUserDto;
import com.nelson.tms.dto.UpdatePasswordDto;
import com.nelson.tms.service.AuthService;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDto createUserDto) {

        authService.createUser(createUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        HttpStatus httpStatus = authService.delete(id);
        return ResponseEntity.status(httpStatus).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER','USER')")
    @PatchMapping("/update-password")
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        HttpStatus httpStatus = authService.updatePassword(updatePasswordDto);
        return ResponseEntity.status(httpStatus).build();
    }
}
