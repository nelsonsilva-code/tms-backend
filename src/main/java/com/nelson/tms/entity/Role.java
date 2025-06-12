package com.nelson.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tms_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 1024)
    private String permissions;

    @Transient
    public Set<Permission> getPermissionSet() {
        return Arrays.stream(permissions.split(","))
                .map(Permission::valueOf)
                .collect(Collectors.toSet());
    }

    @Transient
    public void setPermissionSet(Set<Permission> perms) {
        this.permissions = perms.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }
}
