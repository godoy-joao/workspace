package io.github.godoyjoao.workspace.task.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskModel {
    private UUID idTask;
    private UUID idWorkspace;
    private String status;
    private Boolean archived;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime dueDate;
}
