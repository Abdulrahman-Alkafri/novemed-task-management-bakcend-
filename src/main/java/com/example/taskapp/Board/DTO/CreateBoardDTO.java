package com.example.taskapp.Board.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardDTO {
    @NotBlank(message = "Board name cannot be blank")
    private String name;
    
    private String description;
} 