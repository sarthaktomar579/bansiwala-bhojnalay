package com.bansiwala.bhojnalay.config;

import com.bansiwala.bhojnalay.entity.AppUser;
import com.bansiwala.bhojnalay.enums.UserRole;
import com.bansiwala.bhojnalay.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSetup implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminSetup(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepo.existsByUsername("admin")) {
            AppUser admin = new AppUser(
                    "admin",
                    passwordEncoder.encode("bansiwala2026"),
                    UserRole.ADMIN,
                    null
            );
            userRepo.save(admin);
            System.out.println(">>> Admin user created: username=admin, password=bansiwala2026");
        }
    }
}
