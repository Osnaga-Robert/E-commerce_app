package com.example.shop4All_backend.configurations;

import com.example.shop4All_backend.exceptions.LogInException;
import com.example.shop4All_backend.services.JwtService;
import com.example.shop4All_backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static String CURRENT_USER = "";
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    /*This function filters incoming requests to check for
    a valid JWT token and sets the authentication context
    if the token is valid.*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        String jwtToken = null;
        String userEmail = null;

        if (header != null && header.startsWith("Bearer ")) {
            // Extract JWT token from the header
            jwtToken = header.substring(7);

            try {
                userEmail = jwtUtil.getUserEmailFromToken(jwtToken);
                CURRENT_USER = userEmail;
            } catch (IllegalArgumentException e) {
                throw new LogInException("Invalid JWT");
            } catch (ExpiredJwtException e) {
                throw new LogInException("Jwt token expired");
            }
        } else if (header != null) {
            throw new LogInException("Jwt token doesn't start with Bearer ");
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtService.loadUserByUsername(userEmail);
            // Validate the token
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                // Create authentication token
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Set authentication details
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication context
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

}
