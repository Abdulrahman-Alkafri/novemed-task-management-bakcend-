package com.example.taskapp.SubTask.V1.Service;

import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.SubTask.V1.DTO.CreateSubtaskDTO;
import com.example.taskapp.SubTask.V1.DTO.SubtaskResponseDTO;
import com.example.taskapp.SubTask.V1.DTO.UpdateSubtaskDTO;
import com.example.taskapp.SubTask.V1.Mapper.SubtaskMapper;
import com.example.taskapp.SubTask.V1.Model.Subtask;
import com.example.taskapp.SubTask.V1.Repository.SubtaskRepository;
import com.example.taskapp.Task.V1.Model.Task;
import com.example.taskapp.Task.V1.Repository.TaskRepository;
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
public class SubtaskService {
    private static final Logger logger = LoggerFactory.getLogger(SubtaskService.class);
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final SubtaskMapper subtaskMapper;

    @Cacheable(CacheConfig.SUBTASK_CACHE)
    public List<SubtaskResponseDTO> getAllSubtasks() {
        logger.info("Fetching all subtasks");
        List<SubtaskResponseDTO> subtasks = subtaskRepository.findAll().stream()
                .map(subtaskMapper::toResponseDTO)
                .collect(Collectors.toList());
        logger.debug("Retrieved {} subtasks", subtasks.size());
        return subtasks;
    }

    @Cacheable(value = CacheConfig.SUBTASK_CACHE, key = "#taskId")
    public List<SubtaskResponseDTO> getSubtasksByTaskId(Long taskId) {
        logger.info("Fetching subtasks for task ID: {}", taskId);
        List<SubtaskResponseDTO> subtasks = subtaskRepository.findByTaskId(taskId).stream()
                .map(subtaskMapper::toResponseDTO)
                .collect(Collectors.toList());
        logger.debug("Retrieved {} subtasks for task ID: {}", subtasks.size(), taskId);
        return subtasks;
    }

    @Cacheable(value = CacheConfig.SUBTASK_CACHE, key = "#id")
    public SubtaskResponseDTO getSubtaskById(Long id) {
        logger.info("Fetching subtask with ID: {}", id);
        return subtaskRepository.findById(id)
                .map(subtaskMapper::toResponseDTO)
                .orElseThrow(() -> {
                    logger.error("Subtask not found with ID: {}", id);
                    return new ResourceNotFoundException("Subtask not found with id: " + id);
                });
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.SUBTASK_CACHE, CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public SubtaskResponseDTO createSubtask(CreateSubtaskDTO createSubtaskDTO) {
        logger.info("Creating a new subtask for task ID: {}", createSubtaskDTO.getTaskId());
        Task task = taskRepository.findById(createSubtaskDTO.getTaskId())
                .orElseThrow(() -> {
                    logger.error("Task not found with ID: {}", createSubtaskDTO.getTaskId());
                    return new ResourceNotFoundException("Task not found with id: " + createSubtaskDTO.getTaskId());
                });

        Subtask subtask = subtaskMapper.toEntity(createSubtaskDTO);
        subtask.setTask(task);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        logger.info("Subtask created successfully with ID: {}", savedSubtask.getId());
        return subtaskMapper.toResponseDTO(savedSubtask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.SUBTASK_CACHE, CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public SubtaskResponseDTO updateSubtask(Long id, UpdateSubtaskDTO updateSubtaskDTO) {
        logger.info("Updating subtask with ID: {}", id);
        Subtask existingSubtask = subtaskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Subtask not found with ID: {}", id);
                    return new ResourceNotFoundException("Subtask not found with id: " + id);
                });

        logger.debug("Updating subtask details for ID: {}", id);
        Subtask updatedSubtask = subtaskMapper.toEntity(updateSubtaskDTO, existingSubtask);
        Subtask savedSubtask = subtaskRepository.save(updatedSubtask);
        logger.info("Subtask updated successfully with ID: {}", savedSubtask.getId());
        return subtaskMapper.toResponseDTO(savedSubtask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.SUBTASK_CACHE, CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public void deleteSubtask(Long id) {
        logger.info("Deleting subtask with ID: {}", id);
        if (!subtaskRepository.existsById(id)) {
            logger.error("Subtask not found with ID: {}", id);
            throw new ResourceNotFoundException("Subtask not found with id: " + id);
        }
        subtaskRepository.deleteById(id);
        logger.info("Subtask deleted successfully with ID: {}", id);
    }
}