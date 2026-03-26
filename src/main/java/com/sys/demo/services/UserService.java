package com.sys.demo.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sys.demo.entities.User;
import com.sys.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // ==========================
    // LOGIN
    // ==========================
    public User login(String username, String password) {

        User user = repo.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no existe"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }

    // ==========================
    // SPRING SECURITY
    // ==========================
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> user = repo.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.get().getUsername())
                .password(user.get().getPassword())
                .roles(user.get().getRole())
                .build();
    }

    // ==========================
    // REGISTER
    // ==========================
    public User guardar(User user) {
        return repo.save(user);
    }
}
