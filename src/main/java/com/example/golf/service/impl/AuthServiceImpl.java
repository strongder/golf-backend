package com.example.golf.service.impl;

import com.example.golf.dtos.user.Response.UserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {


    public UserResponse getInfoUserToGoogle(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String id = principal.getAttribute("sub");
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(email);
        userResponse.setFullName(name);
        userResponse.setId(id);
        return userResponse;
    }


}
