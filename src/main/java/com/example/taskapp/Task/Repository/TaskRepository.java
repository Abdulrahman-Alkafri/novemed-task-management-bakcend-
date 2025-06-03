package com.example.taskapp.Task.Repository;

import com.example.taskapp.Task.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByColumnTableIdOrderByOrderAsc(Long columnId);

    @Query("SELECT MAX(t.order) FROM Task t WHERE t.columnTable.id = :columnId")
    Integer findMaxOrderByColumnId(Long columnId);
}