package io.github.godoyjoao.workspace.identity;

import io.github.godoyjoao.workspace.workspace.WorkspaceUsers;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "workspace")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID idUser;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkspaceUsers> memberships = new HashSet<>();
}
