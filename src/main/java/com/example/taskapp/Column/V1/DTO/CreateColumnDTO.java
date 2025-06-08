package com.example.taskapp.Column.V1.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateColumnDTO {
    @NotBlank(message = "Column name cannot be blank")
    private String name;
    
    @NotNull(message = "Board ID cannot be null")
    private Long boardId;
} 