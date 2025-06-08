package com.example.taskapp.Column.V1.Model;

import com.example.taskapp.Board.V1.Model.Board;
import com.example.taskapp.Task.V1.Model.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "columns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Column name cannot be blank")
    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "columnTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}