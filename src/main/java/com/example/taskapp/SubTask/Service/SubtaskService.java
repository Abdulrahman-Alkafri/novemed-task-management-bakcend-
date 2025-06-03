package com.example.taskapp.SubTask.Service;

import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.SubTask.DTO.CreateSubtaskDTO;
import com.example.taskapp.SubTask.DTO.SubtaskResponseDTO;
import com.example.taskapp.SubTask.DTO.UpdateSubtaskDTO;
import com.example.taskapp.SubTask.Mapper.SubtaskMapper;
import com.example.taskapp.SubTask.Model.Subtask;
import com.example.taskapp.SubTask.Repository.SubtaskRepository;
import com.example.taskapp.Task.Model.Task;
import com.example.taskapp.Task.Repository.TaskRepository;
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
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final SubtaskMapper subtaskMapper;

    @Cacheable(CacheConfig.SUBTASK_CACHE)
    public List<SubtaskResponseDTO> getAllSubtasks() {
        return subtaskRepository.findAll().stream()
                .map(subtaskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.SUBTASK_CACHE, key = "#taskId")
    public List<SubtaskResponseDTO> getSubtasksByTaskId(Long taskId) {
        return subtaskRepository.findByTaskId(taskId).stream()
                .map(subtaskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.SUBTASK_CACHE, key = "#id")
    public SubtaskResponseDTO getSubtaskById(Long id) {
        return subtaskRepository.findById(id)
                .map(subtaskMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.SUBTASK_CACHE, CacheConfig.TASK_CACHE}, allEntries = true)
    public SubtaskResponseDTO createSubtask(CreateSubtaskDTO createSubtaskDTO) {
        Task task = taskRepository.findById(createSubtaskDTO.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + createSubtaskDTO.getTaskId()));

        Subtask subtask = subtaskMapper.toEntity(createSubtaskDTO);
        subtask.setTask(task);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        return subtaskMapper.toResponseDTO(savedSubtask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.SUBTASK_CACHE, CacheConfig.TASK_CACHE}, allEntries = true)
    public SubtaskResponseDTO updateSubtask(Long id, UpdateSubtaskDTO updateSubtaskDTO) {
        Subtask existingSubtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id: " + id));

        Subtask updatedSubtask = subtaskMapper.toEntity(updateSubtaskDTO, existingSubtask);
        Subtask savedSubtask = subtaskRepository.save(updatedSubtask);
        return subtaskMapper.toResponseDTO(savedSubtask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.SUBTASK_CACHE, CacheConfig.TASK_CACHE}, allEntries = true)
    public void deleteSubtask(Long id) {
        if (!subtaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subtask not found with id: " + id);
        }
        subtaskRepository.deleteById(id);
    }
}