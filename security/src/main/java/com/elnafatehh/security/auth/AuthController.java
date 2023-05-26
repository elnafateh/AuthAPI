package com.elnafatehh.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticateService authenticateService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authenticateService.register(registerRequest));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> Authenticate(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authenticateService.Authenticate(authRequest));

    }

}
