package com.nelson.tms.dto;

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
    private String email;
    private String password;
    private String role;
}
