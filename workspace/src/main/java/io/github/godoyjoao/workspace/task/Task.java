package io.github.godoyjoao.workspace.task;

import io.github.godoyjoao.workspace.task.dto.TaskModel;
import io.github.godoyjoao.workspace.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks", schema = "workspace")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_workspace")
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Boolean archived;

    @Column(length = 128)
    private String title;

    @Column(length = 1024)
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dueDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    public TaskModel getModel() {
        return new TaskModel(
                this.getIdTask(),
                this.getWorkspace().getIdWorkspace(),
                this.getStatus().name(),
                this.getArchived(),
                this.getTitle(),
                this.getDescription(),
                this.getCreatedAt(),
                this.getCompletedAt(),
                this.getDueDate()
        );
    }

}
