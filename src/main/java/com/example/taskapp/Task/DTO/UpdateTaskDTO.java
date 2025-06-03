package com.example.taskapp.Task.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDTO {
    @NotBlank(message = "Task title cannot be blank")
    private String title;
    
    private String description;
    
    private Integer order;
    
    private Boolean isCompleted;
    
    private Long columnId;
} 