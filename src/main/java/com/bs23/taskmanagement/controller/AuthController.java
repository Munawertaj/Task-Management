package com.bs23.taskmanagement.controller;

import com.bs23.taskmanagement.dto.LoginRequest;
import com.bs23.taskmanagement.dto.RegistrationRequest;
import com.bs23.taskmanagement.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());
    }

    private String dashboardRedirect(Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, Authentication auth) {
        if (isLoggedIn(auth)) {
            return dashboardRedirect(auth);
        }

        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationRequest registrationRequest,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            authService.register(registrationRequest);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, Authentication auth) {
        if (isLoggedIn(auth)) {
            return dashboardRedirect(auth);
        }

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequest loginRequest,
            BindingResult result,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            return "login";
        }

        if (!authService.login(loginRequest, response)) {
            return "redirect:/login?error";
        }

        return "redirect:/login-success";
    }

    @GetMapping("/login-success")
    public String onLoginSuccess(Authentication auth) {
        return dashboardRedirect(auth);
    }
}