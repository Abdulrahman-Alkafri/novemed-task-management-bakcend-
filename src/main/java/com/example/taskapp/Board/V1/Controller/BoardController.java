package com.example.taskapp.Board.V1.Controller;

import com.example.taskapp.Board.V1.DTO.BoardResponseDTO;
import com.example.taskapp.Board.V1.DTO.CreateBoardDTO;
import com.example.taskapp.Board.V1.DTO.UpdateBoardDTO;
import com.example.taskapp.Board.V1.Service.BoardService;
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
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Boards", description = "Endpoints for managing boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    @RateLimiter(name = "boardRateLimiter")
    @Operation(summary = "Get all boards", description = "Retrieve a list of all boards.")
    public ResponseEntity<List<BoardResponseDTO>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "boardRateLimiter")
    @Operation(summary = "Get board by ID", description = "Retrieve a board by its unique ID.")
    public ResponseEntity<BoardResponseDTO> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @PostMapping
    @RateLimiter(name = "boardRateLimiter")
    @Operation(summary = "Create a new board", description = "Create a new board with the provided details.")
    public ResponseEntity<BoardResponseDTO> createBoard(@Valid @RequestBody CreateBoardDTO createBoardDTO) {
        return new ResponseEntity<>(boardService.createBoard(createBoardDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a board", description = "Update an existing board by its ID.")
    public ResponseEntity<BoardResponseDTO> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBoardDTO updateBoardDTO) {
        return ResponseEntity.ok(boardService.updateBoard(id, updateBoardDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a board", description = "Delete a board by its unique ID.")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
} 