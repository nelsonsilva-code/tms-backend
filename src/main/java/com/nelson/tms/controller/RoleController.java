package com.nelson.tms.controller;

import com.nelson.tms.dto.RoleDto;
import com.nelson.tms.entity.Role;
import com.nelson.tms.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping()
    public ResponseEntity<List> getRoles() {
        List<Role> roles = roleService.getRoles();

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@Valid @RequestBody RoleDto roleDto) {
        roleService.createRole(roleDto);

        return ResponseEntity.ok().build();
    }
}
