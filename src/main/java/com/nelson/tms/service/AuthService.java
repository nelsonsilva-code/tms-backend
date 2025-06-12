package com.nelson.tms.service;

import com.nelson.tms.dto.*;
import com.nelson.tms.entity.Permission;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface AuthService {
    void createUser(CreateUserDto createUserDto);

    JwtAuthResponse login(LoginDto loginDto);

    HttpStatus delete(Long id);

    HttpStatus updatePassword(UpdatePasswordDto updatePasswordDto, Long id);

    List<Role> getRoles();

    Role createRole(RoleDto roleDto);

    Permission[] getPermissionList();

    List<User> getUsers();

}
