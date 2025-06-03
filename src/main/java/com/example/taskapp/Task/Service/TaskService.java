package com.example.taskapp.Task.Service;

import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.Column.Model.ColumnTable;
import com.example.taskapp.Column.Repository.ColumnRepository;
import com.example.taskapp.Task.DTO.CreateTaskDTO;
import com.example.taskapp.Task.DTO.TaskResponseDTO;
import com.example.taskapp.Task.DTO.UpdateTaskDTO;
import com.example.taskapp.Task.Mapper.TaskMapper;
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
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;
    private final TaskMapper taskMapper;

    @Cacheable(CacheConfig.TASK_CACHE)
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.TASK_CACHE, key = "#columnId")
    public List<TaskResponseDTO> getTasksByColumnId(Long columnId) {
        return taskRepository.findByColumnTableIdOrderByOrderAsc(columnId).stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.TASK_CACHE, key = "#id")
    public TaskResponseDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
        ColumnTable column = columnRepository.findById(createTaskDTO.getColumnId())
                .orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + createTaskDTO.getColumnId()));

        Task task = taskMapper.toEntity(createTaskDTO);
        task.setColumnTable(column);

        if (task.getOrder() == null) {
            Integer maxOrder = taskRepository.findMaxOrderByColumnId(column.getId());
            task.setOrder(maxOrder != null ? maxOrder + 1 : 0);
        }

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDTO(savedTask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public TaskResponseDTO updateTask(Long id, UpdateTaskDTO updateTaskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (updateTaskDTO.getColumnId() != null && !updateTaskDTO.getColumnId().equals(existingTask.getColumnTable().getId())) {
            ColumnTable newColumn = columnRepository.findById(updateTaskDTO.getColumnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Column not found with id: " + updateTaskDTO.getColumnId()));
            existingTask.setColumnTable(newColumn);
        }

        Task updatedTask = taskMapper.toEntity(updateTaskDTO, existingTask);
        Task savedTask = taskRepository.save(updatedTask);
        return taskMapper.toResponseDTO(savedTask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public void reorderTasks(Long columnId, List<Long> taskIds) {
        List<Task> tasks = taskRepository.findAllById(taskIds);
        if (tasks.size() != taskIds.size()) {
            throw new ResourceNotFoundException("One or more tasks not found");
        }

        for (int i = 0; i < taskIds.size(); i++) {
            Long taskId = taskIds.get(i);
            Task task = tasks.stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
            task.setOrder(i);
        }

        taskRepository.saveAll(tasks);
    }
}