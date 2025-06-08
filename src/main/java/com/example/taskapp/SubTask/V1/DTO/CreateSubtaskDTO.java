package com.example.taskapp.SubTask.V1.DTO;

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
public class CreateSubtaskDTO {
    @NotBlank(message = "Subtask title cannot be blank")
    private String title;
    
    @NotNull(message = "Task ID cannot be null")
    private Long taskId;
    
    private Boolean isCompleted;
} 