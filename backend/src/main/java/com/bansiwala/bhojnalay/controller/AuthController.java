package com.bansiwala.bhojnalay.controller;

import com.bansiwala.bhojnalay.dto.AuthResponse;
import com.bansiwala.bhojnalay.dto.LoginRequest;
import com.bansiwala.bhojnalay.entity.AppUser;
import com.bansiwala.bhojnalay.enums.UserRole;
import com.bansiwala.bhojnalay.repository.AppUserRepository;
import com.bansiwala.bhojnalay.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AppUserRepository userRepo,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var userOpt = userRepo.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }

        AppUser user = userOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name(),
                user.getStudentId()
        );

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                user.getStudentId()
        ));
    }

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudentUser(@RequestBody LoginRequest req,
                                                  @RequestParam Long studentId) {
        if (userRepo.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Username already taken"));
        }

        AppUser user = new AppUser(
                req.getUsername(),
                passwordEncoder.encode(req.getPassword()),
                UserRole.STUDENT,
                studentId
        );
        userRepo.save(user);

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name(),
                user.getStudentId()
        );

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                user.getStudentId()
        ));
    }
}
