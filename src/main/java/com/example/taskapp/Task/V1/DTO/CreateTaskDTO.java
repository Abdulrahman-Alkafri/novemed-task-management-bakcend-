package com.example.taskapp.Task.V1.DTO;

import com.example.taskapp.SubTask.V1.DTO.CreateSubtaskDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private List<CreateSubtaskDTO> subtasks;
}