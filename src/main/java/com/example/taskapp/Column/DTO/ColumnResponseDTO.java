package com.example.taskapp.Column.DTO;

import com.example.taskapp.Task.DTO.TaskResponseDTO;
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
public class ColumnResponseDTO {
    private Long id;
    private String name;
    private Integer order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskResponseDTO> tasks;
} 