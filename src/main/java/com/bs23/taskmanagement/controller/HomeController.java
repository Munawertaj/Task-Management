package com.bs23.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
