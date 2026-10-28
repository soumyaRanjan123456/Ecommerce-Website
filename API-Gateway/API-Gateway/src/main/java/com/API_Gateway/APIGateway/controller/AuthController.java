
package com.API_Gateway.APIGateway.controller;

import com.API_Gateway.APIGateway.config.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping("/status")
    public Map<String, String> authStatus() {
        return Map.of(
                "service", "Authentication",
                "status", "UP",
                "message", "Authentication endpoint is running"
        );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(
            @RequestBody LoginRequest request) {

        return userDetailsService.findByUsername(request.username())
                .filter(user -> passwordEncoder.matches(
                        request.password(), user.getPassword()))
                .map(user -> ResponseEntity.ok(
                        Map.of(
                                "message", "Login successful",
                                "username", user.getUsername(),
                                "tokenType", "Bearer",
                                "accessToken",
                                jwtService.generateToken(user.getUsername())
                        )))
                .defaultIfEmpty(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of(
                                        "error",
                                        "Invalid username or password"
                                )));
    }

    public record LoginRequest(String username, String password) {}
}
