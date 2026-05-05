package io.github.godoyjoao.workspace.workspace;

import io.github.godoyjoao.workspace.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    @Query("SELECT wu.workspace FROM WorkspaceUsers wu WHERE wu.user = :user")
    List<Workspace> findByUser(User user);

    @Query("SELECT w FROM Workspace w JOIN w.memberships wu WHERE w.idWorkspace = :id AND wu.user = :user AND wu.accessLevel <> :revoked")
    Optional<Workspace> findByIdAndUser(UUID id, User user, AccessLevel revoked);
}
