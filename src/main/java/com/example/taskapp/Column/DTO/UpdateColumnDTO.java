package com.example.taskapp.Column.DTO;

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
    @NotBlank(message = "Column name cannot be blank")
    private String name;
    
    private Integer order;
    
    private Long boardId;
} 