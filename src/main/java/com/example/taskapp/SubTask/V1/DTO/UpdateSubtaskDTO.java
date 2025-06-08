package com.example.taskapp.SubTask.V1.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubtaskDTO {
    @NotBlank(message = "Subtask title cannot be blank")
    private String title;
    
    private Boolean isCompleted;
} 