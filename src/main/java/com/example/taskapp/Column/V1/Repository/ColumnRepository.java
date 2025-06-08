package com.example.taskapp.Column.V1.Repository;

import com.example.taskapp.Column.V1.Model.ColumnTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnTable, Long> {
    List<ColumnTable> findByBoardIdOrderByBoardId(Long boardId);

}