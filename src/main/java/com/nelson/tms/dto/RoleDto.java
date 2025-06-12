package com.nelson.tms.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDto {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 1024)
    private String permissions;
}
