package com.nelson.tms.service;

import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.RegisterDto;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

public interface AuthService {
    void register(RegisterDto registerDto);

    HttpStatus login(LoginDto loginDto);

    HttpStatus delete(Long id);
}
