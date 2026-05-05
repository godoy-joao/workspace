package io.github.godoyjoao.workspace.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTask(
        UUID idWorkspace,
        String title,
        String description,
        LocalDateTime dueDate
) {
}
