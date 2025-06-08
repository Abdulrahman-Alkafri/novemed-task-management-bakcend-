package com.example.taskapp.Task.V1.Mapper;

import com.example.taskapp.SubTask.V1.Mapper.SubtaskMapper;
import com.example.taskapp.Task.V1.DTO.CreateTaskDTO;
import com.example.taskapp.Task.V1.DTO.TaskResponseDTO;
import com.example.taskapp.Task.V1.DTO.UpdateTaskDTO;
import com.example.taskapp.Task.V1.Model.Task;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TaskMapper {
    private final SubtaskMapper subtaskMapper;

    public TaskMapper(SubtaskMapper subtaskMapper) {
        this.subtaskMapper = subtaskMapper;
    }

    public Task toEntity(CreateTaskDTO dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public Task toEntity(UpdateTaskDTO dto, Task existingTask) {
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        return existingTask;
    }

    public TaskResponseDTO toResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .subtasks(task.getSubtasks() != null ?
                    task.getSubtasks().stream()
                        .map(subtaskMapper::toResponseDTO)
                        .collect(Collectors.toList()) : null)
                .columnId(task.getColumnTable().getId())
                .columnName(task.getColumnTable().getName())
                .build();
    }
} 