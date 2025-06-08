package com.example.taskapp.Task.V1.DTO;

import com.example.taskapp.SubTask.V1.DTO.SubtaskResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SubtaskResponseDTO> subtasks;
    private Long columnId;
    private String columnName;
} 