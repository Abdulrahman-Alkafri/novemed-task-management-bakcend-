package com.example.taskapp.Column.Repository;

import com.example.taskapp.Column.Model.ColumnTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnTable, Long> {
    List<ColumnTable> findByBoardIdOrderByOrderAsc(Long boardId);

    @Query("SELECT MAX(c.order) FROM ColumnTable c WHERE c.board.id = :boardId")
    Integer findMaxOrderByBoardId(Long boardId);
}