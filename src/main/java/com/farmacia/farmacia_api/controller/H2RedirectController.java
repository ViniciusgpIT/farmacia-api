package com.farmacia.farmacia_api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
@Hidden  // Oculta do Swagger
public class H2RedirectController {
    
    @GetMapping("/h2")
    public void redirectToH2Console(HttpServletResponse response) throws IOException {
        // Verifica se o usuário está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && 
            !"anonymousUser".equals(auth.getPrincipal())) {
            
            // Redireciona para o H2 Console
            response.sendRedirect("/h2-console");
        } else {
            // Redireciona para login
            response.sendRedirect("/login");
        }
    }
    
    @GetMapping("/db-info")
    public String getDatabaseInfo() {
        return """
            <h3>Informações do Banco de Dados H2</h3>
            <p><strong>URL:</strong> jdbc:h2:mem:farmacia_db</p>
            <p><strong>Usuário:</strong> sa</p>
            <p><strong>Senha:</strong> (vazio)</p>
            <p><a href="/h2-console">Acessar H2 Console</a></p>
            <p><a href="/swagger-ui/index.html">Acessar Swagger</a></p>
            """;
    }
}   