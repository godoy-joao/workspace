package io.github.godoyjoao.workspace.identity;

import org.springframework.data.jpa.repository.JpaRepository;

// To be properly implemented yet, no current use for global roles, but it is a good practice to have it in place for future use
public interface RoleRepository extends JpaRepository<Role, Long> {
}
