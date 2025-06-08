package com.example.taskapp.Task.V1.DTO;

import com.example.taskapp.SubTask.V1.DTO.UpdateSubtaskDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDTO {
    private String title;
    private String description;
    private Long columnId;
    private List<UpdateSubtaskDTO> subtasks;
} 