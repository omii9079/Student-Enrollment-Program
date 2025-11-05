package com.ediest.programenrollment.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    String token = null;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {


        String path = req.getServletPath();
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/create-super-admin")) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

     
        try {
            var claims = jwtUtil.Validate(token).getBody();
            String email = claims.getSubject();
            String role = (String) claims.get("role");
            // simpleGrantedauthority is a Spring class that reprsents one role or authority which user has  , simpleGrantedAuthority represents a role or permission that a user has in spring Security
            var authorities = List.of((new SimpleGrantedAuthority("ROLE_" + role)));
            Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
            // new UsernamePasswordAuthenticationToken it hold user info with deails and implements authentication interface after we would store it Secuirtycontextholder is just locker we would set authenticated user details with in it
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());


        } catch (Exception e) {
            log.error("User is not valid or token is tampered ");

        }
        chain.doFilter(req, res);
    }


}
