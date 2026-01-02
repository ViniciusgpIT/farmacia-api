package com.farmacia.farmacia_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Retorna login.html do diretório templates
    }
}