package com.example.taskapp.Task.V1.Controller;

import com.example.taskapp.Task.V1.DTO.CreateTaskDTO;
import com.example.taskapp.Task.V1.DTO.TaskResponseDTO;
import com.example.taskapp.Task.V1.DTO.UpdateTaskDTO;
import com.example.taskapp.Task.V1.Service.TaskService;
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
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @RateLimiter(name = "taskRateLimiter")
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks.")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/column/{columnId}")
    @RateLimiter(name = "taskRateLimiter")
    @Operation(summary = "Get tasks by column ID", description = "Retrieve tasks associated with a specific column.")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByColumnId(@PathVariable Long columnId) {
        return ResponseEntity.ok(taskService.getTasksByColumnId(columnId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a task by its unique ID.")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @RateLimiter(name = "taskRateLimiter")
    @Operation(summary = "Create a new task", description = "Create a new task with the provided details.")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        return new ResponseEntity<>(taskService.createTask(createTaskDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Update an existing task by its ID.")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDTO updateTaskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, updateTaskDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete a task by its unique ID.")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
} 