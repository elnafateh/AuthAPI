package com.elnafatehh.security.auth;

import com.elnafatehh.security.config.JwtService;
import com.elnafatehh.security.user.Role;
import com.elnafatehh.security.user.User;
import com.elnafatehh.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest registerRequest) {
    var user= User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
    userRepository.save(user);
    var jwtToken =jwtService.generateToken(user);
    return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse Authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
       var user=userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
        var jwtToken =jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }
}
