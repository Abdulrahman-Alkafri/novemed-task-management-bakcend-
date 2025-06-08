package com.example.taskapp.Column.V1.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateColumnDTO {
    private Long id;
    
    @NotBlank(message = "Column name cannot be blank")
    private String name;
        
    private Long boardId;
} 