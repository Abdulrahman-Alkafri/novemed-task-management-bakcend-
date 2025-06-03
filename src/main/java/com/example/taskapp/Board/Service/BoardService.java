package com.example.taskapp.Board.Service;

import com.example.taskapp.Board.DTO.BoardResponseDTO;
import com.example.taskapp.Board.DTO.CreateBoardDTO;
import com.example.taskapp.Board.DTO.UpdateBoardDTO;
import com.example.taskapp.Board.Mapper.BoardMapper;
import com.example.taskapp.Board.Model.Board;
import com.example.taskapp.Board.Repository.BoardRepository;
import com.example.taskapp.Config.CacheConfig;
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
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Cacheable(CacheConfig.BOARD_CACHE)
    public List<BoardResponseDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(boardMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.BOARD_CACHE, key = "#id")
    public BoardResponseDTO getBoardById(Long id) {
        return boardRepository.findById(id)
                .map(boardMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = CacheConfig.BOARD_CACHE, allEntries = true)
    public BoardResponseDTO createBoard(CreateBoardDTO createBoardDTO) {
        Board board = boardMapper.toEntity(createBoardDTO);
        Board savedBoard = boardRepository.save(board);
        return boardMapper.toResponseDTO(savedBoard);
    }

    @Transactional
    @CacheEvict(value = CacheConfig.BOARD_CACHE, allEntries = true)
    public BoardResponseDTO updateBoard(Long id, UpdateBoardDTO updateBoardDTO) {
        Board existingBoard = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));

        Board updatedBoard = boardMapper.toEntity(updateBoardDTO, existingBoard);
        Board savedBoard = boardRepository.save(updatedBoard);
        return boardMapper.toResponseDTO(savedBoard);
    }

    @Transactional
    @CacheEvict(value = CacheConfig.BOARD_CACHE, allEntries = true)
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Board not found with id: " + id);
        }
        boardRepository.deleteById(id);
    }
}