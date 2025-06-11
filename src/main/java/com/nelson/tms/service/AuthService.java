package com.nelson.tms.service;

import com.nelson.tms.dto.JwtAuthResponse;
import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.CreateUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

public interface AuthService {
    void createUser(CreateUserDto createUserDto, Authentication authentication);

    JwtAuthResponse login(LoginDto loginDto);

    HttpStatus delete(Long id);
}
