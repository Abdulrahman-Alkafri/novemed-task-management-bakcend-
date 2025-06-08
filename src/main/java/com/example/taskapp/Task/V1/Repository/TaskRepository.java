package com.example.taskapp.Task.V1.Repository;

import com.example.taskapp.Task.V1.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByColumnTableIdOrderByColumnTableId(Long columnId);
}