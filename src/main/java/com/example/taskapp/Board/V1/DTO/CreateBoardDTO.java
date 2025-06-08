package com.example.taskapp.Board.V1.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardDTO {
    @NotBlank(message = "Board name cannot be blank")
    private String name;
    
    private String description;
    
    private List<String> columnNames; // Optional list of column names to create with the board
} 