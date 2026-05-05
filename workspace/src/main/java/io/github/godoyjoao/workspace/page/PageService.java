package io.github.godoyjoao.workspace.page;

import io.github.godoyjoao.workspace.identity.User;
import io.github.godoyjoao.workspace.page.dto.CreatePage;
import io.github.godoyjoao.workspace.page.dto.PageView;
import io.github.godoyjoao.workspace.page.dto.UpdatePage;
import io.github.godoyjoao.workspace.workspace.Workspace;
import io.github.godoyjoao.workspace.workspace.WorkspaceService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final EntityManager entityManager;
    private final WorkspaceService workspaceService;

    public void createPage(CreatePage request, UUID idWorkspace, UUID idUser) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);
        User user = entityManager.getReference(User.class, idUser);
        Page parent = entityManager.getReference(Page.class, request.idParent());

        Page page = new Page();

        page.setCreator(user);
        page.setWorkspace(workspace);
        page.setCreatedAt(LocalDateTime.now());
        page.setUpdatedAt(LocalDateTime.now());
        page.setTitle(request.title());

        if (parent != null) page.setParent(parent);

        pageRepository.save(page);
    }

    public List<PageView> findPageViews(UUID idWorkspace, UUID idUser) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        if (workspace == null) throw new IllegalStateException("Workspace not found");

        return pageRepository.findViewsByWorkspace(workspace);
    }

    public Page findPage(UUID idPage, UUID idWorkspace, UUID idUser) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        if (workspace == null) throw new IllegalStateException("Workspace not found");

        return pageRepository.findByIdAndWorkspace(idPage, workspace).orElse(null);
    }

    public List<PageView> findChildrenView(UUID idPage, UUID idWorkspace, UUID idUser) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        if (workspace == null) throw new IllegalStateException("Workspace not found");

        Page parent = entityManager.getReference(Page.class, idPage);

        return pageRepository.findChildrenViewByWorkspaceAndParent(workspace, parent);
    }

    public void updatePage(UUID idPage, UUID idWorkspace, UUID idUser, UpdatePage request) {
        workspaceService.ensureUserIsParticipant(idWorkspace, idUser);

        Workspace workspace = entityManager.getReference(Workspace.class, idWorkspace);

        if (workspace == null) throw new IllegalStateException("Workspace not found");

        Page page = pageRepository.findByIdAndWorkspace(idPage, workspace).orElse(null);

        if (page == null) throw new IllegalStateException("Page not found");

        page.setTitle(request.title());
        page.setContent(request.content());
        page.setUpdatedAt(LocalDateTime.now());

        pageRepository.save(page);
    }
}
