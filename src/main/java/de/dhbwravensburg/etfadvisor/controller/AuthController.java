package de.dhbwravensburg.etfadvisor.controller;

import de.dhbwravensburg.etfadvisor.dto.AuthRequest;
import de.dhbwravensburg.etfadvisor.dto.AuthResponse;
import de.dhbwravensburg.etfadvisor.dto.RegisterRequest;
import de.dhbwravensburg.etfadvisor.entity.Role;
import de.dhbwravensburg.etfadvisor.entity.User;
import de.dhbwravensburg.etfadvisor.repository.UserRepository;
import de.dhbwravensburg.etfadvisor.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository repository,PasswordEncoder encoder, JwtUtil util){
        this.userRepository = repository;
        this.passwordEncoder = encoder;
        this.jwtUtil = util;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request){
        var password = passwordEncoder.encode(request.password());
        var user = new User(null,request.username(),password, Role.valueOf(request.role()));
        userRepository.save(user);
        var token = jwtUtil.generateToken(user.getUsername(),user.getRole().toString());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        var user = userRepository.findByUsername(request.username()).orElseThrow(()->new UsernameNotFoundException("User not found"));
        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid password");
        }
        return new AuthResponse(jwtUtil.generateToken(user.getUsername(),user.getRole().toString()));
    }
}
