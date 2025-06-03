package com.example.taskapp.Column.Mapper;

import com.example.taskapp.Column.DTO.ColumnResponseDTO;
import com.example.taskapp.Column.DTO.CreateColumnDTO;
import com.example.taskapp.Column.DTO.UpdateColumnDTO;
import com.example.taskapp.Column.Model.ColumnTable;
import com.example.taskapp.Task.Mapper.TaskMapper;
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
                .order(dto.getOrder())
                .build();
    }

    public ColumnTable toEntity(UpdateColumnDTO dto, ColumnTable existingColumn) {
        existingColumn.setName(dto.getName());
        existingColumn.setOrder(dto.getOrder());
        return existingColumn;
    }

    public ColumnResponseDTO toResponseDTO(ColumnTable column) {
        return ColumnResponseDTO.builder()
                .id(column.getId())
                .name(column.getName())
                .order(column.getOrder())
                .createdAt(column.getCreatedAt())
                .updatedAt(column.getUpdatedAt())
                .tasks(column.getTasks() != null ?
                    column.getTasks().stream()
                        .map(taskMapper::toResponseDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }
} 