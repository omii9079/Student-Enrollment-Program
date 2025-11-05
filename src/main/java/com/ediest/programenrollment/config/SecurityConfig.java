package com.ediest.programenrollment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement((s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                //auth is object you use inside the lambda is an instance of the class AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry this class is like Rule Builder this gives you methods like such as requestmatcher , permitall ...when we call it we are adding the security rule into Spring internal list
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/create-super-admin", "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/create-admin").hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/org/**").hasRole("SUPER_ADMIN")  // hasrole internally add like ROLE_ SUPER_ADMIN
                        .requestMatchers("/api/program/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        .requestMatchers("/api/enroll/**").authenticated()
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //UsernamePasswordAuthenticationFilter It handles login requests (with username & password) in classic form-based authentication That’s the filter that normally:
        //Reads username & password from a login form (or request body)
        //Authenticates user through AuthenticationManager
        //Creates a session if successful and then Sees that user is already authenticated (from JWT) , Skips login process
        return http.build();
    }

    @Bean
    // we are creating the object of password encoder to use it in controller so we can encrypt the password using this object by their methods we have configured here
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
