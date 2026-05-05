package io.github.godoyjoao.workspace.identity;

import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles", schema = "workspace")
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    @Column(unique = true, nullable = false)
    private String name;

    @Override
    public @Nullable String getAuthority() {
        return this.name;
    }
}
