package com.example.taskapp.Board.V1.Service;

import com.example.taskapp.Board.V1.DTO.BoardResponseDTO;
import com.example.taskapp.Board.V1.DTO.CreateBoardDTO;
import com.example.taskapp.Board.V1.DTO.UpdateBoardDTO;
import com.example.taskapp.Board.V1.Mapper.BoardMapper;
import com.example.taskapp.Board.V1.Model.Board;
import com.example.taskapp.Board.V1.Repository.BoardRepository;
import com.example.taskapp.Column.V1.Model.ColumnTable;
import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Cacheable(CacheConfig.BOARD_CACHE)
    public List<BoardResponseDTO> getAllBoards() {
        logger.info("Fetching all boards");
        return boardRepository.findAll().stream()
                .map(boardMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.BOARD_CACHE, key = "#id")
    public BoardResponseDTO getBoardById(Long id) {
        logger.info("Fetching board with ID: {}", id);
        return boardRepository.findById(id)
                .map(boardMapper::toResponseDTO)
                .orElseThrow(() -> {
                    logger.error("Board not found with ID: {}", id);
                    return new ResourceNotFoundException("Board not found with id: " + id);
                });
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.BOARD_CACHE, CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE, CacheConfig.SUBTASK_CACHE}, allEntries = true)
    public BoardResponseDTO createBoard(CreateBoardDTO createBoardDTO) {
        logger.info("Creating a new board with name: {}", createBoardDTO.getName());
        Board board = boardMapper.toEntity(createBoardDTO);
        Board savedBoard = boardRepository.save(board);
        logger.info("Board created successfully with ID: {}", savedBoard.getId());
        return boardMapper.toResponseDTO(savedBoard);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.BOARD_CACHE, CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE, CacheConfig.SUBTASK_CACHE}, allEntries = true)
    public BoardResponseDTO updateBoard(Long id, UpdateBoardDTO updateBoardDTO) {
        logger.info("Updating board with ID: {}", id);
        Board existingBoard = boardRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Board not found with ID: {}", id);
                    return new ResourceNotFoundException("Board not found with id: " + id);
                });

        if (updateBoardDTO.getName() != null) {
            logger.debug("Updating board name to: {}", updateBoardDTO.getName());
            existingBoard.setName(updateBoardDTO.getName());
        }
        if (updateBoardDTO.getDescription() != null) {
            logger.debug("Updating board description");
            existingBoard.setDescription(updateBoardDTO.getDescription());
        }

        if (updateBoardDTO.getColumns() != null) {
            logger.debug("Updating columns for board with ID: {}", id);
            List<ColumnTable> existingColumns = existingBoard.getColumns();
            if (existingColumns == null) {
                existingColumns = new ArrayList<>();
                existingBoard.setColumns(existingColumns);
            }

            Map<Long, ColumnTable> existingColumnMap = existingColumns.stream()
                    .collect(Collectors.toMap(ColumnTable::getId, column -> column));

            List<ColumnTable> updatedColumns = updateBoardDTO.getColumns().stream()
                    .map(columnDTO -> {
                        ColumnTable column;
                        if (columnDTO.getId() != null && existingColumnMap.containsKey(columnDTO.getId())) {
                            logger.debug("Updating existing column with ID: {}", columnDTO.getId());
                            column = existingColumnMap.get(columnDTO.getId());
                            column.setName(columnDTO.getName());
                        } else {
                            logger.debug("Creating new column with name: {}", columnDTO.getName());
                            column = ColumnTable.builder()
                                    .name(columnDTO.getName())
                                    .board(existingBoard)
                                    .build();
                        }
                        return column;
                    })
                    .toList();

            existingColumns.clear();
            existingColumns.addAll(updatedColumns);
        }

        Board savedBoard = boardRepository.save(existingBoard);
        logger.info("Board updated successfully with ID: {}", savedBoard.getId());
        return boardMapper.toResponseDTO(savedBoard);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.BOARD_CACHE, CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE, CacheConfig.SUBTASK_CACHE}, allEntries = true)
    public void deleteBoard(Long id) {
        logger.info("Deleting board with ID: {}", id);
        if (!boardRepository.existsById(id)) {
            logger.error("Board not found with ID: {}", id);
            throw new ResourceNotFoundException("Board not found with id: " + id);
        }
        boardRepository.deleteById(id);
        logger.info("Board deleted successfully with ID: {}", id);
    }
}