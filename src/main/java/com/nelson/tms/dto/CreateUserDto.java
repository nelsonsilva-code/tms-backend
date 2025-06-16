package com.nelson.tms.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserDto {

    private Long id;

    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{1,}$",
            message = "First name must be only letters, spaces, apostrophes or hyphens."
    )
    private String name;

    @Pattern(
            regexp = "^[a-z0-9_]{1,12}$",
            message = "Username must be at most 12 characters long, contain only lowercase letters, no spaces and only allowed special characters: _"
    )
    private String username;

    private String role;
}
