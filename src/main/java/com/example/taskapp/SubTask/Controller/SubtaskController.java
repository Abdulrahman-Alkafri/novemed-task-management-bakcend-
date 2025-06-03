package com.example.taskapp.SubTask.Controller;

import com.example.taskapp.SubTask.DTO.CreateSubtaskDTO;
import com.example.taskapp.SubTask.DTO.SubtaskResponseDTO;
import com.example.taskapp.SubTask.DTO.UpdateSubtaskDTO;
import com.example.taskapp.SubTask.Service.SubtaskService;
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
@RequestMapping("/api/subtasks")
@RequiredArgsConstructor
@Tag(name = "Subtasks", description = "Endpoints for managing subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;

    @GetMapping
    @RateLimiter(name = "subtaskRateLimiter")
    @Operation(summary = "Get all subtasks", description = "Retrieve a list of all subtasks.")
    public ResponseEntity<List<SubtaskResponseDTO>> getAllSubtasks() {
        return ResponseEntity.ok(subtaskService.getAllSubtasks());
    }

    @GetMapping("/task/{taskId}")
    @RateLimiter(name = "subtaskRateLimiter")
    @Operation(summary = "Get subtasks by task ID", description = "Retrieve subtasks associated with a specific task.")
    public ResponseEntity<List<SubtaskResponseDTO>> getSubtasksByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(subtaskService.getSubtasksByTaskId(taskId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subtask by ID", description = "Retrieve a subtask by its unique ID.")
    public ResponseEntity<SubtaskResponseDTO> getSubtaskById(@PathVariable Long id) {
        return ResponseEntity.ok(subtaskService.getSubtaskById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new subtask", description = "Create a new subtask with the provided details.")
    public ResponseEntity<SubtaskResponseDTO> createSubtask(@Valid @RequestBody CreateSubtaskDTO createSubtaskDTO) {
        return new ResponseEntity<>(subtaskService.createSubtask(createSubtaskDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a subtask", description = "Update an existing subtask by its ID.")
    public ResponseEntity<SubtaskResponseDTO> updateSubtask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubtaskDTO updateSubtaskDTO) {
        return ResponseEntity.ok(subtaskService.updateSubtask(id, updateSubtaskDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subtask", description = "Delete a subtask by its unique ID.")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long id) {
        subtaskService.deleteSubtask(id);
        return ResponseEntity.noContent().build();
    }
} 