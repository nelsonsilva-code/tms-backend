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
    private String name;
    private String username;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.{12,}$)(?!.*\\s)[A-Za-z\\d!@_-]+$",
            message = "Password must be at least 12 characters long, contain at least one uppercase letter, no spaces, and only allowed special characters: ! @ _ -"
    )

    private String password;
    private String role;
}
