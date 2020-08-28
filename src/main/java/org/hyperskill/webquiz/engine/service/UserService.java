package org.hyperskill.webquiz.engine.service;

import org.hyperskill.webquiz.engine.model.User;
import org.hyperskill.webquiz.engine.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Component
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Email already taken!")
    static class EmailTakenException extends RuntimeException {
    }

    public void registerUser(User user) {
        if (userRepository.existsById(user.getEmail())) {
            throw new EmailTakenException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (!userRepository.existsById(user.getEmail())) {
            throw new UsernameNotFoundException(email + " not found");
        }
        return new org.springframework.security.core.userdetails.
                User(user.getEmail(), user.getPassword(), List.of());
    }
}