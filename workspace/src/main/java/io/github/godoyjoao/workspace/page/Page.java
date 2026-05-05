package io.github.godoyjoao.workspace.page;

import io.github.godoyjoao.workspace.identity.User;
import io.github.godoyjoao.workspace.page.dto.PageModel;
import io.github.godoyjoao.workspace.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pages", schema = "workspace")
@Data
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_workspace", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent")
    private Page parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creator", nullable = false)
    private User creator;

    private String title;

    @Column(columnDefinition = "jsonb")
    private String content;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    public PageModel getModel() {
        return new PageModel(
                this.getIdPage(),
                this.getParent().getIdPage(),
                this.getCreator().getIdUser(),
                this.getTitle(),
                this.getContent(),
                this.getUpdatedAt(),
                this.getCreatedAt(),
                this.getDeletedAt()
        );
    }
}
