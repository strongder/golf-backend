package com.example.golf.controller;

import com.example.golf.dtos.auth.request.AuthRequest;
import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.auth.response.AuthResponse;
import com.example.golf.dtos.user.Request.CreateUserRequest;
import com.example.golf.service.impl.UserServiceImpl;
import com.example.golf.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/google-url")
    public ResponseEntity<String> getGoogleLoginUrl(HttpServletRequest request) {
        String redirectUri = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/oauth2/authorization/google")
                .toUriString();
        return ResponseEntity.ok(redirectUri);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        log.info("authRequest: {}", authRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateToken(authRequest.getEmail());
            AuthResponse authResponse = new AuthResponse("Đăng nhập thành công", token);
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during authentication: {}", e.getMessage());
            return new ResponseEntity<>(new AuthResponse("Đăng nhập thất bại", null), HttpStatus.UNAUTHORIZED);
        }
    }

   // register
    @PostMapping("/register")
    public ApiResponse register(@RequestBody CreateUserRequest request) {
        return ApiResponse.success(userServiceImpl.createGolfer(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        // Implement your logout logic here
        // For example, invalidate the JWT token
        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

}
