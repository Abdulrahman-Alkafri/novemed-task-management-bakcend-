package com.example.taskapp.Task.DTO;

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
public class CreateTaskDTO {
    @NotBlank(message = "Task title cannot be blank")
    private String title;
    
    private String description;
    
    @NotNull(message = "Column ID cannot be null")
    private Long columnId;
    
    private Integer order;
} 