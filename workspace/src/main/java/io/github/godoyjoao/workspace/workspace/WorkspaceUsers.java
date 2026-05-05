package io.github.godoyjoao.workspace.workspace;

import io.github.godoyjoao.workspace.identity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "workspace_users", schema = "workspace", uniqueConstraints = @UniqueConstraint(columnNames = {"id_user", "id_workspace"}))
@Data
public class WorkspaceUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idWorkspaceUser;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_workspace")
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;
}
