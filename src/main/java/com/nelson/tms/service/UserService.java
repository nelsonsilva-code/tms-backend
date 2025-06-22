package com.nelson.tms.service;

import com.nelson.tms.dto.CreateUserDto;
import com.nelson.tms.dto.UpdatePasswordDto;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface UserService {
    void createUser(CreateUserDto createUserDto);
    HttpStatus delete(Long id);
    HttpStatus updatePassword(UpdatePasswordDto updatePasswordDto, Long id);
    List<User> getUsers();
    User getUser(Long id);
    User getCurrentUser();
    HttpStatus updateRole(String newRole, Long userId);
}
