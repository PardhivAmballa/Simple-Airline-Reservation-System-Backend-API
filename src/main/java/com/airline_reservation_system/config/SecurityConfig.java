package com.airline_reservation_system.config;

import com.airline_reservation_system.service.CustomUserDetailsService;
import com.airline_reservation_system.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Inject the custom user details service to load user info (username, password, roles)
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Password encoder bean using BCrypt for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Define the main security filter chain for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        LogUtil.system("Loading Security Configuration...");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow registration endpoint to be accessed without authentication
                        .requestMatchers("/api/users/register").permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                .httpBasic(customizer -> customizer
                        .authenticationEntryPoint((request, response, authException) -> {
                            LogUtil.error("UNAUTHORIZED attempt on: " + request.getRequestURI());
                            response.sendError(401, "Unauthorized");
                        })
                )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            LogUtil.system("LOGIN SUCCESS: " + authentication.getName());
                        })
                        .failureHandler((request, response, exception) -> {
                            String user = request.getParameter("username");
                            LogUtil.error("LOGIN FAILED for user: " + user);
                            response.sendError(401, "Invalid Credentials");
                        })
                );

        LogUtil.system("Security Configuration successfully initialized.");

        return http.build();
    }
}