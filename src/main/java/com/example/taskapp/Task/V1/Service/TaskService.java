package com.example.taskapp.Task.V1.Service;

import com.example.taskapp.Config.CacheConfig;
import com.example.taskapp.Column.V1.Model.ColumnTable;
import com.example.taskapp.Column.V1.Repository.ColumnRepository;
import com.example.taskapp.Task.V1.DTO.CreateTaskDTO;
import com.example.taskapp.Task.V1.DTO.TaskResponseDTO;
import com.example.taskapp.Task.V1.DTO.UpdateTaskDTO;
import com.example.taskapp.Task.V1.Mapper.TaskMapper;
import com.example.taskapp.Task.V1.Model.Task;
import com.example.taskapp.Task.V1.Repository.TaskRepository;
import com.example.taskapp.SubTask.V1.Model.Subtask;
import com.example.taskapp.SubTask.V1.Mapper.SubtaskMapper;
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
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;
    private final TaskMapper taskMapper;
    private final SubtaskMapper subtaskMapper;

    @Cacheable(CacheConfig.TASK_CACHE)
    public List<TaskResponseDTO> getAllTasks() {
        logger.info("Fetching all tasks");
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.TASK_CACHE, key = "#columnId")
    public List<TaskResponseDTO> getTasksByColumnId(Long columnId) {
        logger.info("Fetching tasks for column with ID: {}", columnId);
        return taskRepository.findByColumnTableIdOrderByColumnTableId(columnId).stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.TASK_CACHE, key = "#id")
    public TaskResponseDTO getTaskById(Long id) {
        logger.info("Fetching task with ID: {}", id);
        return taskRepository.findById(id)
                .map(taskMapper::toResponseDTO)
                .orElseThrow(() -> {
                    logger.error("Task not found with ID: {}", id);
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
        logger.info("Creating a new task with title: {}", createTaskDTO.getTitle());
        ColumnTable column = columnRepository.findById(createTaskDTO.getColumnId())
                .orElseThrow(() -> {
                    logger.error("Column not found with ID: {}", createTaskDTO.getColumnId());
                    return new ResourceNotFoundException("Column not found with id: " + createTaskDTO.getColumnId());
                });

        Task task = taskMapper.toEntity(createTaskDTO);
        task.setColumnTable(column);

        if (createTaskDTO.getSubtasks() != null && !createTaskDTO.getSubtasks().isEmpty()) {
            logger.debug("Adding subtasks to the task");
            List<Subtask> subtasks = createTaskDTO.getSubtasks().stream()
                    .map(subtaskDTO -> {
                        Subtask subtask = subtaskMapper.toEntity(subtaskDTO);
                        subtask.setTask(task);
                        return subtask;
                    })
                    .collect(Collectors.toList());
            task.setSubtasks(subtasks);
        }

        Task savedTask = taskRepository.save(task);
        logger.info("Task created successfully with ID: {}", savedTask.getId());
        return taskMapper.toResponseDTO(savedTask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE, CacheConfig.SUBTASK_CACHE}, allEntries = true)
    public TaskResponseDTO updateTask(Long id, UpdateTaskDTO updateTaskDTO) {
        logger.info("Updating task with ID: {}", id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found with ID: {}", id);
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });

        if (updateTaskDTO.getColumnId() != null && !updateTaskDTO.getColumnId().equals(existingTask.getColumnTable().getId())) {
            logger.debug("Moving task to column with ID: {}", updateTaskDTO.getColumnId());
            ColumnTable newColumn = columnRepository.findById(updateTaskDTO.getColumnId())
                    .orElseThrow(() -> {
                        logger.error("Column not found with ID: {}", updateTaskDTO.getColumnId());
                        return new ResourceNotFoundException("Column not found with id: " + updateTaskDTO.getColumnId());
                    });
            existingTask.setColumnTable(newColumn);
        }

        if (updateTaskDTO.getTitle() != null) {
            logger.debug("Updating task title to: {}", updateTaskDTO.getTitle());
            existingTask.setTitle(updateTaskDTO.getTitle());
        }
        if (updateTaskDTO.getDescription() != null) {
            logger.debug("Updating task description");
            existingTask.setDescription(updateTaskDTO.getDescription());
        }

        if (updateTaskDTO.getSubtasks() != null) {
            logger.debug("Updating subtasks for task with ID: {}", id);
            List<Subtask> existingSubtasks = existingTask.getSubtasks();
            if (existingSubtasks == null) {
                existingSubtasks = new ArrayList<>();
                existingTask.setSubtasks(existingSubtasks);
            }

            Map<String, Subtask> existingSubtaskMap = existingSubtasks.stream()
                    .collect(Collectors.toMap(Subtask::getTitle, subtask -> subtask));

            List<Subtask> updatedSubtasks = updateTaskDTO.getSubtasks().stream()
                    .map(subtaskDTO -> {
                        Subtask subtask;
                        if (existingSubtaskMap.containsKey(subtaskDTO.getTitle())) {
                            logger.debug("Updating existing subtask with title: {}", subtaskDTO.getTitle());
                            subtask = existingSubtaskMap.get(subtaskDTO.getTitle());
                            subtask.setIsCompleted(subtaskDTO.getIsCompleted());
                        } else {
                            logger.debug("Creating new subtask with title: {}", subtaskDTO.getTitle());
                            subtask = subtaskMapper.toEntity(subtaskDTO, new Subtask());
                            subtask.setTask(existingTask);
                        }
                        return subtask;
                    })
                    .toList();

            existingSubtasks.clear();
            existingSubtasks.addAll(updatedSubtasks);
        }

        Task savedTask = taskRepository.save(existingTask);
        logger.info("Task updated successfully with ID: {}", savedTask.getId());
        return taskMapper.toResponseDTO(savedTask);
    }

    @Transactional
    @CacheEvict(value = {CacheConfig.TASK_CACHE, CacheConfig.COLUMN_CACHE}, allEntries = true)
    public void deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        if (!taskRepository.existsById(id)) {
            logger.error("Task not found with ID: {}", id);
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
        logger.info("Task deleted successfully with ID: {}", id);
    }
}