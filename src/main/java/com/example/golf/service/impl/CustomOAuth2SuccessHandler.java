package com.example.golf.service.impl;

import com.example.golf.enums.GuestType;
import com.example.golf.enums.UserRole;
import com.example.golf.model.Guest;
import com.example.golf.model.User;
import com.example.golf.repository.GuestRepository;
import com.example.golf.repository.UserRepository;
import com.example.golf.utils.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${app.redirect-url}")
    private  String REDIRECT_URL;
    private final GuestRepository guestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        // Tạo user nếu chưa tồn tại
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setActive(true);
            user.setFullName(oauthUser.getAttribute("name"));
            user.setRole(UserRole.MEMBER);
            user.setProvider("GOOGLE");
            userRepository.save(user);

            Guest guest = new Guest();
//            guest.setAvatar(oauthUser.getAttribute("picture"));
            guest.setUserId(user.getId());
            guest.setFullName(user.getFullName());
            guest.setEmail(user.getEmail());
            guest.setPhone(oauthUser.getAttribute("phone_number"));
            guest.setDeleted(false);
            guest.setRole(GuestType.GOLFER);
            guestRepository.save(guest);
        }

        // Sinh JWT
        String token = jwtUtils.generateToken(user.getEmail());
        // Redirect về FE kèm token
        String redirectUrl = REDIRECT_URL +"?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}
