package de.dhbwravensburg.etfadvisor.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailService service;
    private final JwtUtil util;

    public JwtAuthenticationFilter(CustomUserDetailService service, JwtUtil util){
        this.service =service;
        this.util = util;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("Authorization")==null || !request.getHeader("Authorization").startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        var token = request.getHeader("Authorization").substring(7);
        if(util.isTokenValid(token)&& SecurityContextHolder.getContext().getAuthentication()==null){
            var username = util.extractUsername(token);
            var user = service.loadUserByUsername(username);
            var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request,response);

    }
}
