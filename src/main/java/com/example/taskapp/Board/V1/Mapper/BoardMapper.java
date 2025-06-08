package com.example.taskapp.Board.V1.Mapper;

import com.example.taskapp.Board.V1.DTO.BoardResponseDTO;
import com.example.taskapp.Board.V1.DTO.CreateBoardDTO;
import com.example.taskapp.Board.V1.DTO.UpdateBoardDTO;
import com.example.taskapp.Board.V1.Model.Board;
import com.example.taskapp.Column.V1.Mapper.ColumnMapper;
import com.example.taskapp.Column.V1.Model.ColumnTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardMapper {
    private final ColumnMapper columnMapper;

    public BoardMapper(ColumnMapper columnMapper) {
        this.columnMapper = columnMapper;
    }

    public Board toEntity(CreateBoardDTO dto) {
        Board board = Board.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        // If column names are provided, create columns for the board
        if (dto.getColumnNames() != null && !dto.getColumnNames().isEmpty()) {
            List<ColumnTable> columns = new ArrayList<>();
            for (int i = 0; i < dto.getColumnNames().size(); i++) {
                String columnName = dto.getColumnNames().get(i);
                ColumnTable column = ColumnTable.builder()
                        .name(columnName)
                        .board(board)
                        .build();
                columns.add(column);
            }
            board.setColumns(columns);
        }

        return board;
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