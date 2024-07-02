package com.eventscheduler.EventScheduler.filter;

import com.eventscheduler.EventScheduler.service.UserService;
import com.eventscheduler.EventScheduler.util.JwtUtil;
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
public class JwtRequestFilter extends OncePerRequestFilter { // this extends ensures that the filter is executed once per request

    @Autowired
    private UserService userService; // used to load user details

    @Autowired
    private JwtUtil jwtUtil; // used to handle JWT operations

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization"); // retrieves authorization header from HTTP Request

        String username = null;

        String jwt = null;

        // checks if the authorization header is present and extracts the JWT token by removing the Bearer
        // uses the jwtUtil to extract the username
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // checks if the username is not null and if the current authentication contect is not set
        // loads user details using userService and validates the token
        // if valid then it creates a UsernamePasswordAuthenticationToken and sets it in the SecurityContextHolder
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // passes the request and response to the next filter in the chain, ensuring the request processing continues
        filterChain.doFilter(request, response);
    }
}