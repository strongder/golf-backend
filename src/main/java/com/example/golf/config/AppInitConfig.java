package com.example.golf.config;

import com.example.golf.enums.UserRole;
import com.example.golf.model.User;
import com.example.golf.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppInitConfig {

    PasswordEncoder passwordEncoder;
    @NonFinal
    private String ADMIN_USER_NAME = "admin@gmail.com";

    @NonFinal
    private String ADMIN_USER_PASS = "admin";
    @Bean
    public ApplicationRunner applicationRunner (UserRepository userRepository)
    {
        return args -> {
            if(userRepository.findByEmail(ADMIN_USER_NAME).isEmpty()) {
                User admin = userRepository.save(
                        User.builder()
                                .email(ADMIN_USER_NAME)
                                .password(passwordEncoder.encode(ADMIN_USER_PASS))
//                                .avatar("https://th.bing.com/th/id/R.22dbc0f5e5f5648613f0d1de3ea7ae0a?rik=k6HQ45uVGe81rw&pid=ImgRaw&r=0")
                                .role(UserRole.ADMIN)
                                .build()
                );
            }
        };
    }
}
