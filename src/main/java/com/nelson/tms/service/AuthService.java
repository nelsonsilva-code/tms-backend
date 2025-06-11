package com.nelson.tms.service;

import com.nelson.tms.dto.JwtAuthResponse;
import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.CreateUserDto;
import org.springframework.http.HttpStatus;

public interface AuthService {
    void createUser(CreateUserDto createUserDto);

    JwtAuthResponse login(LoginDto loginDto);

    HttpStatus delete(Long id);
}
