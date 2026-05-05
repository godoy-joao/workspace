package io.github.godoyjoao.workspace.page;

import io.github.godoyjoao.workspace.page.dto.PageView;
import io.github.godoyjoao.workspace.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PageRepository extends JpaRepository<Page, UUID> {

    List<Page> findByWorkspace(Workspace workspace);

    /**
     * @param workspace
     * @return Root pages for the provided workspace
     */
    @Query("""
            SELECT p.idPage AS idPage, p.title AS title, (COUNT(c) > 0) AS hasChildren
                        FROM Page p 
                        LEFT JOIN Page c ON c.parent = p 
                        WHERE p.workspace = :workspace 
                        AND p.parent IS NULL 
                        GROUP BY p.idPage, p.title""")
    List<PageView> findViewsByWorkspace(Workspace workspace);

    /**
     *
     * @param workspace
     * @param parent
     * @return children for provided parent page
     */
    @Query("""
            SELECT p.idPage AS idPage, p.title AS title, (COUNT(c) > 0) AS hasChildren
                        FROM Page p 
                        LEFT JOIN Page c ON c.parent = p 
                        WHERE p.workspace = :workspace 
                        AND p.parent = :parent 
                        GROUP BY p.idPage, p.title""")
    List<PageView> findChildrenViewByWorkspaceAndParent(Workspace workspace, Page parent);

    List<Page> findByWorkspaceAndParent(Workspace workspace, Page parent);

    Optional<Page> findByIdAndWorkspace(UUID idPage, Workspace workspace);
}
