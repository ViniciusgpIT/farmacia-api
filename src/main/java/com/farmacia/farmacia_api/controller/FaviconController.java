package com.farmacia.farmacia_api.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FaviconController {
    
    @GetMapping("/favicon.ico")
    public void favicon(HttpServletResponse response) throws IOException {
        // Retorna 204 No Content quando o favicon não existe
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}

