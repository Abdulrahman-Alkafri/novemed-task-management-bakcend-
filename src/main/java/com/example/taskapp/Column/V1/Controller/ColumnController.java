package com.example.taskapp.Column.V1.Controller;

import com.example.taskapp.Column.V1.DTO.ColumnResponseDTO;
import com.example.taskapp.Column.V1.DTO.CreateColumnDTO;
import com.example.taskapp.Column.V1.DTO.UpdateColumnDTO;
import com.example.taskapp.Column.V1.Service.ColumnService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/columns")
@RateLimiter(name = "columnRateLimiter")
@RequiredArgsConstructor
@Tag(name = "Columns", description = "Endpoints for managing columns")
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @Operation(summary = "Get all columns", description = "Retrieve a list of all columns.")
    public ResponseEntity<List<ColumnResponseDTO>> getAllColumns() {
        return ResponseEntity.ok(columnService.getAllColumns());
    }

    @GetMapping("/board/{boardId}")
    @Operation(summary = "Get columns by board ID", description = "Retrieve columns associated with a specific board.")
    public ResponseEntity<List<ColumnResponseDTO>> getColumnsByBoardId(@PathVariable Long boardId) {
        return ResponseEntity.ok(columnService.getColumnsByBoardId(boardId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get column by ID", description = "Retrieve a column by its unique ID.")
    public ResponseEntity<ColumnResponseDTO> getColumnById(@PathVariable Long id) {
        return ResponseEntity.ok(columnService.getColumnById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new column", description = "Create a new column with the provided details.")
    public ResponseEntity<ColumnResponseDTO> createColumn(@Valid @RequestBody CreateColumnDTO createColumnDTO) {
        return new ResponseEntity<>(columnService.createColumn(createColumnDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a column", description = "Update an existing column by its ID.")
    public ResponseEntity<ColumnResponseDTO> updateColumn(
            @PathVariable Long id,
            @Valid @RequestBody UpdateColumnDTO updateColumnDTO) {
        return ResponseEntity.ok(columnService.updateColumn(id, updateColumnDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a column", description = "Delete a column by its unique ID.")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long id) {
        columnService.deleteColumn(id);
        return ResponseEntity.noContent().build();
    }
} 