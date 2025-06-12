package com.nelson.tms.service;

import com.nelson.tms.dto.JwtAuthResponse;
import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.CreateUserDto;
import com.nelson.tms.dto.UpdatePasswordDto;
import com.nelson.tms.entity.Role;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface AuthService {
    void createUser(CreateUserDto createUserDto);

    JwtAuthResponse login(LoginDto loginDto);

    HttpStatus delete(Long id);

    HttpStatus updatePassword(UpdatePasswordDto updatePasswordDto);

    List<Role> getRoles();
}
