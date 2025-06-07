package com.nelson.tms.service;

import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.RegisterDto;
import org.springframework.http.HttpStatus;

public interface AuthService {
    void register(RegisterDto registerDto);

    HttpStatus login(LoginDto loginDto);

}
