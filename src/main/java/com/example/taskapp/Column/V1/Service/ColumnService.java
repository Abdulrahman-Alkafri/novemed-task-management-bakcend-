package com.example.taskapp.Column.V1.Service;

import com.example.taskapp.Board.V1.Model.Board;
import com.example.taskapp.Board.V1.Repository.BoardRepository;
import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.Column.V1.DTO.ColumnResponseDTO;
import com.example.taskapp.Column.V1.DTO.CreateColumnDTO;
import com.example.taskapp.Column.V1.DTO.UpdateColumnDTO;
import com.example.taskapp.Column.V1.Mapper.ColumnMapper;
import com.example.taskapp.Column.V1.Model.ColumnTable;
import com.example.taskapp.Column.V1.Repository.ColumnRepository;
import com.example.taskapp.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private static final Logger logger = LoggerFactory.getLogger(ColumnService.class);
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final ColumnMapper columnMapper;

    @Cacheable(CacheConfig.COLUMN_CACHE)
    public List<ColumnResponseDTO> getAllColumns() {
        logger.info("Fetching all columns");
        return columnRepository.findAll().stream()
                .map(columnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.COLUMN_CACHE, key = "#boardId")
    public List<ColumnResponseDTO> getColumnsByBoardId(Long boardId) {
        logger.info("Fetching columns for board with ID: {}", boardId);
        return columnRepository.findByBoardIdOrderByBoardId(boardId).stream()
                .map(columnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.COLUMN_CACHE, key = "#id")
    public ColumnResponseDTO getColumnById(Long id) {
        logger.info("Fetching column with ID: {}", id);
        return columnRepository.findById(id)
                .map(columnMapper::toResponseDTO)
                .orElseThrow(() -> {
                    logger.error("Column not found with ID: {}", id);
                    return new ResourceNotFoundException("Column not found with id: " + id);
                });
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE}, allEntries = true)
    public ColumnResponseDTO createColumn(CreateColumnDTO createColumnDTO) {
        logger.info("Creating a new column with name: {}", createColumnDTO.getName());
        Board board = boardRepository.findById(createColumnDTO.getBoardId())
                .orElseThrow(() -> {
                    logger.error("Board not found with ID: {}", createColumnDTO.getBoardId());
                    return new ResourceNotFoundException("Board not found with id: " + createColumnDTO.getBoardId());
                });

        ColumnTable column = columnMapper.toEntity(createColumnDTO);
        column.setBoard(board);

        ColumnTable savedColumn = columnRepository.save(column);
        logger.info("Column created successfully with ID: {}", savedColumn.getId());
        return columnMapper.toResponseDTO(savedColumn);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE, CacheConfig.SUBTASK_CACHE}, allEntries = true)
    public ColumnResponseDTO updateColumn(Long id, UpdateColumnDTO updateColumnDTO) {
        logger.info("Updating column with ID: {}", id);
        ColumnTable existingColumn = columnRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Column not found with ID: {}", id);
                    return new ResourceNotFoundException("Column not found with id: " + id);
                });

        if (updateColumnDTO.getBoardId() != null && !updateColumnDTO.getBoardId().equals(existingColumn.getBoard().getId())) {
            logger.debug("Moving column to board with ID: {}", updateColumnDTO.getBoardId());
            Board newBoard = boardRepository.findById(updateColumnDTO.getBoardId())
                    .orElseThrow(() -> {
                        logger.error("Board not found with ID: {}", updateColumnDTO.getBoardId());
                        return new ResourceNotFoundException("Board not found with id: " + updateColumnDTO.getBoardId());
                    });
            existingColumn.setBoard(newBoard);
        }

        ColumnTable updatedColumn = columnMapper.toEntity(updateColumnDTO, existingColumn);
        ColumnTable savedColumn = columnRepository.save(updatedColumn);
        logger.info("Column updated successfully with ID: {}", savedColumn.getId());
        return columnMapper.toResponseDTO(savedColumn);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE, CacheConfig.TASK_CACHE, CacheConfig.SUBTASK_CACHE}, allEntries = true)
    public void deleteColumn(Long id) {
        logger.info("Deleting column with ID: {}", id);
        ColumnTable column = columnRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Column not found with ID: {}", id);
                    return new ResourceNotFoundException("Column not found with id: " + id);
                });

        // Clear any associations first
        logger.debug("Clearing tasks associated with column ID: {}", id);
        column.getTasks().clear();
        columnRepository.save(column);

        // Now delete the column
        columnRepository.deleteById(id);
        logger.info("Column deleted successfully with ID: {}", id);
    }
}