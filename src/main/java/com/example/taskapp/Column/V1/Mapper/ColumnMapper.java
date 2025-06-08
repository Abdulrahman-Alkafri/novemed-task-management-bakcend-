package com.example.taskapp.Column.V1.Mapper;

import com.example.taskapp.Column.V1.DTO.ColumnResponseDTO;
import com.example.taskapp.Column.V1.DTO.CreateColumnDTO;
import com.example.taskapp.Column.V1.DTO.UpdateColumnDTO;
import com.example.taskapp.Column.V1.Model.ColumnTable;
import com.example.taskapp.Task.V1.Mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ColumnMapper {
    private final TaskMapper taskMapper;

    public ColumnMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public ColumnTable toEntity(CreateColumnDTO dto) {
        return ColumnTable.builder()
                .name(dto.getName())
                .build();
    }

    public ColumnTable toEntity(UpdateColumnDTO dto, ColumnTable existingColumn) {
        existingColumn.setName(dto.getName());
        return existingColumn;
    }

    public ColumnResponseDTO toResponseDTO(ColumnTable column) {
        return ColumnResponseDTO.builder()
                .id(column.getId())
                .name(column.getName())
                .createdAt(column.getCreatedAt())
                .updatedAt(column.getUpdatedAt())
                .tasks(column.getTasks() != null ?
                    column.getTasks().stream()
                        .map(taskMapper::toResponseDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }
} 