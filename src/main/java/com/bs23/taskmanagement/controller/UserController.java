package com.bs23.taskmanagement.controller;

import com.bs23.taskmanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userDetails(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "user/profile";
    }
}
