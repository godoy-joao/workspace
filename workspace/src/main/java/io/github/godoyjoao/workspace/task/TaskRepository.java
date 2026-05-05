package io.github.godoyjoao.workspace.task;

import io.github.godoyjoao.workspace.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByWorkspace(Workspace workspace);

    Optional<Task> findByWorkspaceAndIdTask(Workspace workspace, UUID idTask);
}
