package io.github.Guimaraes131.vroom.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public void create(User user) {
        String password = user.getPassword();
        user.setPassword(encoder.encode(password));

        repository.save(user);
    }

    public User getByLogin(String login) {
        return repository.findByLogin(login);
    }
}
