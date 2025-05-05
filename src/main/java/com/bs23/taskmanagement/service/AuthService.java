package com.bs23.taskmanagement.service;

import com.bs23.taskmanagement.dto.LoginRequest;
import com.bs23.taskmanagement.dto.RegistrationRequest;
import com.bs23.taskmanagement.security.JwtService;
import com.bs23.taskmanagement.util.JwtCookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegistrationRequest registrationRequest) throws IllegalArgumentException {
        userService.registerUser(registrationRequest);
    }

    public boolean login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Generate token and set cookie
            String token = jwtService.generateToken(authentication.getName());
            JwtCookieUtil.addJwtCookie(response, token);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    public void logout(HttpServletResponse response) {
        JwtCookieUtil.clearJwtCookie(response);
    }
}
