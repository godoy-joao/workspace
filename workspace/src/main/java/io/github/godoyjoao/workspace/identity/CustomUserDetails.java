package io.github.godoyjoao.workspace.identity;

import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class CustomUserDetails implements UserDetails {

    private UUID idUser;

    private String username;
    private String password;

    private Set<GrantedAuthority> authorities;

    CustomUserDetails(User user) {
        if (user == null) {
            return;
        }
        this.idUser = user.getIdUser();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getAuthority())
        ).collect(Collectors.toSet());
    }

    public CustomUserDetails(UUID idUser, String username, List<String> authorities) {
        this.idUser = idUser;
        this.username = username;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
