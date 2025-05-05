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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            authService.register(registrationRequest);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("registrationRequest", registrationRequest);
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
                        BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "login";
        }

        boolean successful = authService.login(loginRequest, response);
        if (!successful) {
            System.out.println("Login failed");
            return "redirect:/login?error";
        }

        return "redirect:/login-success";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "redirect:/login?logout";
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }
}