package com.example.userservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user-service")
public class WebController {

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/password-clear")
    public String passwordClearPage() {
        return "password-clear";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "info";
    }

    @GetMapping("/admin")
    public String adminPage() { return "admin"; }
}