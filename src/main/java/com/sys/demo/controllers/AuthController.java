package com.sys.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.dto.AuthResponse;
import com.sys.demo.dto.LoginRequest;
import com.sys.demo.entities.User;
import com.sys.demo.services.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    // ==========================
    // REGISTER
    // ==========================
    @PostMapping("/register")
    public AuthResponse register(@RequestBody User user) {

        // Encriptar password
        user.setPassword(
                encoder.encode(user.getPassword())
        );

        // Rol por defecto
        user.setRole("ADMIN"); // luego puedes cambiar a USER

        User saved = userService.guardar(user);

        // Respuesta sin password
        return new AuthResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole()
        );
    }

    // ==========================
    // LOGIN PERSONALIZADO
    // ==========================
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userService.login(
                request.getUsername(),
                request.getPassword()
        );

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
