package com.example.taskapp.Board.V1.Repository;

import com.example.taskapp.Board.V1.Model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}