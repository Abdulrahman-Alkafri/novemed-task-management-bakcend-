package com.example.taskapp.SubTask.V1.Mapper;

import com.example.taskapp.SubTask.V1.DTO.CreateSubtaskDTO;
import com.example.taskapp.SubTask.V1.DTO.SubtaskResponseDTO;
import com.example.taskapp.SubTask.V1.DTO.UpdateSubtaskDTO;
import com.example.taskapp.SubTask.V1.Model.Subtask;
import org.springframework.stereotype.Component;

@Component
public class SubtaskMapper {
    public Subtask toEntity(CreateSubtaskDTO dto) {
        return Subtask.builder()
                .title(dto.getTitle())
                .isCompleted(dto.getIsCompleted() != null ? dto.getIsCompleted() : false)
                .build();
    }

    public Subtask toEntity(UpdateSubtaskDTO dto, Subtask existingSubtask) {
        existingSubtask.setTitle(dto.getTitle());
        if (dto.getIsCompleted() != null) {
            existingSubtask.setIsCompleted(dto.getIsCompleted());
        }
        return existingSubtask;
    }

    public SubtaskResponseDTO toResponseDTO(Subtask subtask) {
        return SubtaskResponseDTO.builder()
                .id(subtask.getId())
                .title(subtask.getTitle())
                .isCompleted(subtask.getIsCompleted())
                .createdAt(subtask.getCreatedAt())
                .updatedAt(subtask.getUpdatedAt())
                .build();
    }
} 