package io.github.godoyjoao.workspace.workspace;

import io.github.godoyjoao.workspace.identity.User;
import io.github.godoyjoao.workspace.page.Page;
import io.github.godoyjoao.workspace.task.Task;
import io.github.godoyjoao.workspace.workspace.dto.WorkspaceView;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "workspaces", schema = "workspace")
@Data
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idWorkspace;

    @OneToMany(mappedBy = "workspace")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Page> pages = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkspaceUsers> memberships = new HashSet<>();

    private String name;


    public WorkspaceView getView() {
        return new WorkspaceView(this.idWorkspace, this.name);
    }

    public void addMember(User user, AccessLevel accessLevel) {
        WorkspaceUsers membership = new WorkspaceUsers();
        membership.setUser(user);
        membership.setWorkspace(this);
        membership.setAccessLevel(accessLevel);

        this.memberships.add(membership);
        user.getMemberships().add(membership);
    }

}
