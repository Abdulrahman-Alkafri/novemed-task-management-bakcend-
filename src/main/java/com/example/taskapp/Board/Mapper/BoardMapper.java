package com.example.taskapp.Board.Mapper;

import com.example.taskapp.Board.DTO.BoardResponseDTO;
import com.example.taskapp.Board.DTO.CreateBoardDTO;
import com.example.taskapp.Board.DTO.UpdateBoardDTO;
import com.example.taskapp.Board.Model.Board;
import com.example.taskapp.Column.Mapper.ColumnMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BoardMapper {
    private final ColumnMapper columnMapper;

    public BoardMapper(ColumnMapper columnMapper) {
        this.columnMapper = columnMapper;
    }

    public Board toEntity(CreateBoardDTO dto) {
        return Board.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public Board toEntity(UpdateBoardDTO dto, Board existingBoard) {
        existingBoard.setName(dto.getName());
        existingBoard.setDescription(dto.getDescription());
        return existingBoard;
    }

    public BoardResponseDTO toResponseDTO(Board board) {
        return BoardResponseDTO.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .columns(board.getColumns() != null ? 
                    board.getColumns().stream()
                        .map(columnMapper::toResponseDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }
} 