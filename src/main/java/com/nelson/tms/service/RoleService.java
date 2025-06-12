package com.nelson.tms.service;

import com.nelson.tms.dto.RoleDto;
import com.nelson.tms.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    void createRole(RoleDto roleDto);
}