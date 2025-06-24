package com.pe.platform.shared.infrastructure.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pe.platform.shared.infrastructure.security.model.AuthenticatedUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Usa exactamente el mismo secret con el que generaste el token
    private static final String SECRET_KEY = "WriteHereYourSecretStringForTokenSigningCredentials"; // 32 caracteres

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("🔥 Filtro JWT interceptó la petición");

        final String authHeader = request.getHeader("Authorization");
        System.out.println("🛂 Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();        } catch (RuntimeException e) {
            // Firma inválida o token corrupto
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Object idClaim = claims.get("userId");
        if (idClaim == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        long userId;
        try {
            userId = Long.parseLong(idClaim.toString());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Extract username (optional)
        String username = (String) claims.get("sub");
        
        // Extract roles/authorities from JWT (if available)
        List<String> authorities = extractAuthorities(claims);
        
        // Create authenticated user
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, username, authorities);
        
        // Create Spring Security authorities
        var springAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var authToken = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, springAuthorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
    
    @SuppressWarnings("unchecked")
    private List<String> extractAuthorities(Claims claims) {
        try {
            // Try to extract roles from different possible claim names
            Object rolesClaim = claims.get("roles");
            if (rolesClaim == null) {
                rolesClaim = claims.get("authorities");
            }
            if (rolesClaim == null) {
                rolesClaim = claims.get("scope");
            }
              if (rolesClaim instanceof List<?> list) {
                return (List<String>) list;
            } else if (rolesClaim instanceof String stringClaim) {
                return List.of(stringClaim.split(","));
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error extracting authorities from JWT: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}