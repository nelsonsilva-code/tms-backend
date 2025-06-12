package com.nelson.tms.controller;

import com.nelson.tms.entity.Permission;
import com.nelson.tms.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/permissions")
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping()
    public ResponseEntity<Permission[]> getPermissionList() {
        Permission[] permissions = permissionService.getPermissionList();

        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

}
