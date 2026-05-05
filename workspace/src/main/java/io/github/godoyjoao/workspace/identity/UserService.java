package io.github.godoyjoao.workspace.identity;

import io.github.godoyjoao.workspace.identity.dto.CreateAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public void register(CreateAccount request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new DuplicateKeyException("Username is already in use");
        }
        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    public User findUserById(UUID idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!")));
    }
}
