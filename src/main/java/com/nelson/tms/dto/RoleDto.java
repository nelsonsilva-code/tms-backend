package com.nelson.tms.dto;

import com.nelson.tms.entity.Permission;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDto {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 1024)
    private Set<Permission> permissions;
}
