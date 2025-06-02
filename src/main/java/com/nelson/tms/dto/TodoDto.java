package com.nelson.tms.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TodoDto {

    private Long id;
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private boolean complete;
}
