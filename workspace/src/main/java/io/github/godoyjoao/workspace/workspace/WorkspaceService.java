package io.github.godoyjoao.workspace.workspace;

import io.github.godoyjoao.workspace.exception.TenantViolationException;
import io.github.godoyjoao.workspace.identity.User;
import io.github.godoyjoao.workspace.workspace.dto.CreateWorkspace;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final EntityManager entityManager;

    @Transactional
    public void create(CreateWorkspace request, UUID idUser) {

        User user = entityManager.find(User.class, idUser);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Workspace workspace = new Workspace();
        workspace.setName(request.name());
        workspace.addMember(user, AccessLevel.OWNER);

        entityManager.persist(workspace);
    }

    /**
     * @param idUser id of the user
     * @return The workspaces that this user has access
     */
    public List<Workspace> findWorkspacesByUser(UUID idUser) {
        User user = entityManager.getReference(User.class, idUser);
        return workspaceRepository.findByUser(user);
    }

    /**
     * @param idWorkspace id of the workspace
     * @param idUser id of the user to verify
     * @return The specified workspace if the user has access to it
     */
    public Workspace findWorkspace(UUID idWorkspace, UUID idUser) {
        User user = entityManager.getReference(User.class, idUser);
        return workspaceRepository.findByIdAndUser(idWorkspace, user, AccessLevel.REVOKED).orElse(null);
    }

    /**
     * @param idWorkspace id of the workspace
     * @param idUser id of the user to verify
     * @return Whether this workspace exists and the provided user has access to it
     */
    public Boolean isParticipant(UUID idWorkspace, UUID idUser) {
        User user = entityManager.getReference(User.class, idUser);
        return workspaceRepository.findByIdAndUser(idWorkspace, user, AccessLevel.REVOKED).isPresent();
    }

    public void ensureUserIsParticipant(UUID idWorkspace, UUID idUser) {
        if (!isParticipant(idWorkspace, idUser)) {
            throw new TenantViolationException(TenantViolationException.NOT_PARTICIPANT);
        }
    }
}
