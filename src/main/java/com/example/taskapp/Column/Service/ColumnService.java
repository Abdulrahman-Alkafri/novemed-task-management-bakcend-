package com.example.taskapp.Column.Service;

import com.example.taskapp.Board.Model.Board;
import com.example.taskapp.Board.Repository.BoardRepository;
import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.Column.DTO.ColumnResponseDTO;
import com.example.taskapp.Column.DTO.CreateColumnDTO;
import com.example.taskapp.Column.DTO.UpdateColumnDTO;
import com.example.taskapp.Column.Mapper.ColumnMapper;
import com.example.taskapp.Column.Model.ColumnTable;
import com.example.taskapp.Column.Repository.ColumnRepository;
import com.example.taskapp.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final ColumnMapper columnMapper;

    @Cacheable(CacheConfig.COLUMN_CACHE)
    public List<ColumnResponseDTO> getAllColumns() {
        return columnRepository.findAll().stream()
                .map(columnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.COLUMN_CACHE, key = "#boardId")
    public List<ColumnResponseDTO> getColumnsByBoardId(Long boardId) {
        return columnRepository.findByBoardIdOrderByOrderAsc(boardId).stream()
                .map(columnMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.COLUMN_CACHE, key = "#id")
    public ColumnResponseDTO getColumnById(Long id) {
        return columnRepository.findById(id)
                .map(columnMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE}, allEntries = true)
    public ColumnResponseDTO createColumn(CreateColumnDTO createColumnDTO) {
        Board board = boardRepository.findById(createColumnDTO.getBoardId())
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + createColumnDTO.getBoardId()));

        ColumnTable column = columnMapper.toEntity(createColumnDTO);
        column.setBoard(board);

        if (column.getOrder() == null) {
            Integer maxOrder = columnRepository.findMaxOrderByBoardId(board.getId());
            column.setOrder(maxOrder != null ? maxOrder + 1 : 0);
        }

        ColumnTable savedColumn = columnRepository.save(column);
        return columnMapper.toResponseDTO(savedColumn);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE}, allEntries = true)
    public ColumnResponseDTO updateColumn(Long id, UpdateColumnDTO updateColumnDTO) {
        ColumnTable existingColumn = columnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + id));

        if (updateColumnDTO.getBoardId() != null && !updateColumnDTO.getBoardId().equals(existingColumn.getBoard().getId())) {
            Board newBoard = boardRepository.findById(updateColumnDTO.getBoardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + updateColumnDTO.getBoardId()));
            existingColumn.setBoard(newBoard);
        }

        ColumnTable updatedColumn = columnMapper.toEntity(updateColumnDTO, existingColumn);
        ColumnTable savedColumn = columnRepository.save(updatedColumn);
        return columnMapper.toResponseDTO(savedColumn);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE}, allEntries = true)
    public void deleteColumn(Long id) {
        if (!columnRepository.existsById(id)) {
            throw new ResourceNotFoundException("Column not found with id: " + id);
        }
        columnRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.COLUMN_CACHE, CacheConfig.BOARD_CACHE}, allEntries = true)
    public void reorderColumns(Long boardId, List<Long> columnIds) {
        List<ColumnTable> columns = columnRepository.findAllById(columnIds);
        if (columns.size() != columnIds.size()) {
            throw new ResourceNotFoundException("One or more columns not found");
        }
        for (int i = 0; i < columnIds.size(); i++) {
            Long columnId = columnIds.get(i);
            ColumnTable column = columns.stream()
                    .filter(c -> c.getId().equals(columnId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + columnId));
            column.setOrder(i);
        }
        columnRepository.saveAll(columns);
    }
}