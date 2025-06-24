package com.pe.platform.shared.infrastructure.security.model;

import java.util.Collection;
import java.util.Collections;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an authenticated user in the microservice context
 * This replaces the dependency on UserDetailsImpl from the IAM service
 */
@Data
@NoArgsConstructor
public class AuthenticatedUser {
    private Long id;
    private String username;
    private Collection<String> authorities;
    
    public AuthenticatedUser(Long id) {
        this.id = id;
        this.username = null;
        this.authorities = Collections.emptyList();
    }
    
    public AuthenticatedUser(Long id, Collection<String> authorities) {
        this.id = id;
        this.username = null;
        this.authorities = authorities != null ? authorities : Collections.emptyList();
    }
    
    public AuthenticatedUser(Long id, String username, Collection<String> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities != null ? authorities : Collections.emptyList();
    }
    
    public boolean hasAuthority(String authority) {
        return authorities.contains(authority);
    }
    
    public boolean hasRole(String role) {
        return authorities.contains("ROLE_" + role) || authorities.contains(role);
    }
}
