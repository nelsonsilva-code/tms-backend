package com.nelson.tms.service;

import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.RegisterDto;
import org.springframework.http.HttpStatus;

public interface AuthService {
    void register(RegisterDto registerDto);

    String login(LoginDto loginDto);

    HttpStatus delete(Long id);
}
