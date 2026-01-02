package com.farmacia.farmacia_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desabilita CSRF (necessário para H2 Console e APIs REST)
            .csrf(csrf -> csrf.disable())
            
            // 2. Configura as regras de autorização
            .authorizeHttpRequests(authz -> authz
                // Permite acesso público a estes recursos
                .requestMatchers(
                    "/h2-console/**",          // H2 Console
                    "/swagger-ui/**",          // Swagger UI
                    "/v3/api-docs/**",         // OpenAPI docs
                    "/favicon.ico",            // Favicon
                    "/error",                  // Páginas de erro
                    "/api/test/**",            // Endpoints de teste
                    "/login",                  // Página de login (GET)
                    "/static/**"               // Recursos estáticos
                ).permitAll()
                
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            
            // 3. Configura autenticação por formulário e HTTP Basic
            .formLogin(form -> form
                .loginPage("/login")           // Página de login customizada
                .loginProcessingUrl("/login")  // URL para processar o POST do formulário
                .defaultSuccessUrl("/home", true)  // Redireciona para /home após login bem-sucedido
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())  // Mantém HTTP Basic para APIs REST
            
            // 4. Configura headers para permitir H2 Console
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            );
        
        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("farmacia")
            .password(passwordEncoder().encode("admin123"))
            .roles("USER")
            .build();
        
        return new InMemoryUserDetailsManager(user);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}