package com.haotianxu.twitterlike.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
public class UserSecurityConfig {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/users/**", "/api/posts", "api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/manage/**", "/api/manage/**/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/posts", "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/posts", "/api/posts/**", "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/manage/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/manage/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/manage/**").permitAll()
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSRF)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
