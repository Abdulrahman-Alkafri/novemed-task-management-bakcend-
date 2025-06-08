package com.example.taskapp.Board.V1.DTO;

import com.example.taskapp.Column.V1.DTO.UpdateColumnDTO;
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
public class UpdateBoardDTO {
    @NotBlank(message = "Board name cannot be blank")
    private String name;
    
    private String description;
    
    private List<UpdateColumnDTO> columns;
} 