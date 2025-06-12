package com.nelson.tms.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    private String oldPassword;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.{12,}$)(?!.*\\s)[A-Za-z\\d!@_-]+$",
            message = "Password must be at least 12 characters long, contain at least one uppercase letter, no spaces, and only allowed special characters: ! @ _ -"
    )
    private String newPassword;
}
