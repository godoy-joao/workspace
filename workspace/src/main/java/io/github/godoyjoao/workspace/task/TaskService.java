package io.github.godoyjoao.workspace.task;

import io.github.godoyjoao.workspace.task.dto.CreateTask;
import io.github.godoyjoao.workspace.workspace.Workspace;
import io.github.godoyjoao.workspace.workspace.WorkspaceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final EntityManager entityManager;
    private final WorkspaceService workspaceService;

    public void createTask(CreateTask createTask, UUID idUser) {
        if (!workspaceService.isParticipant(createTask.idWorkspace(), idUser)) {
            throw new IllegalArgumentException("Workspace not found");
        }

        if (createTask.description().length() > 1024 || createTask.title().length() > 128) {
            throw new IllegalArgumentException("Description or Title length exceed max size");
        }

        Workspace workspace = entityManager.getReference(Workspace.class, createTask.idWorkspace());

        Task task = new Task();

        task.setTitle(createTask.title());
        task.setDescription(createTask.description());
        task.setDueDate(createTask.dueDate());
        task.setWorkspace(workspace);

        //Default values
        task.setArchived(false);
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        task.setCompletedAt(null);

        taskRepository.save(task);
    }

    public List<Task> findTasks(UUID idWorkspace, UUID idUser) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        return taskRepository.findByWorkspace(workspace);
    }

    public Task findTask(UUID idWorkspace, UUID idUser, UUID idTask) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        return taskRepository.findByWorkspaceAndIdTask(workspace, idTask).orElse(null);
    }

    public void updateTaskStatus(UUID idTask, UUID idWorkspace, UUID idUser, TaskStatus status) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        Task task = taskRepository.findByWorkspaceAndIdTask(workspace, idTask).orElseThrow(() -> new EntityNotFoundException("Task not found."));

        task.setStatus(status);
        if (status == TaskStatus.COMPLETE) {
            task.setCompletedAt(LocalDateTime.now());
        }

        taskRepository.save(task);
    }

    public void archiveTask(UUID idTask, UUID idWorkspace, UUID idUser) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        Task task = taskRepository.findByWorkspaceAndIdTask(workspace, idTask).orElseThrow(() -> new EntityNotFoundException("Task not found."));

        task.setArchived(!task.getArchived());

        taskRepository.save(task);
    }
}
