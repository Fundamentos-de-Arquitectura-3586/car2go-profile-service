package com.pe.platform.shared.infrastructure.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pe.platform.shared.infrastructure.security.model.AuthenticatedUser;

/**
 * Utility class for extracting authenticated user information from the security context
 */
public final class AuthenticationUtils {
    
    private AuthenticationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Gets the currently authenticated user
     * @return the authenticated user
     * @throws IllegalStateException if no user is authenticated or the principal is not an AuthenticatedUser
     */
    public static AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        if (!(authentication.getPrincipal() instanceof AuthenticatedUser)) {
            throw new IllegalStateException("Principal is not an AuthenticatedUser");
        }
        
        return (AuthenticatedUser) authentication.getPrincipal();
    }
    
    /**
     * Gets the ID of the currently authenticated user
     * @return the user ID
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
    
    /**
     * Checks if the current user has a specific authority
     * @param authority the authority to check
     * @return true if the user has the authority, false otherwise
     */
    public static boolean hasAuthority(String authority) {
        try {
            return getCurrentUser().hasAuthority(authority);
        } catch (IllegalStateException e) {
            return false;
        }
    }
    
    /**
     * Checks if the current user has a specific role
     * @param role the role to check (without ROLE_ prefix)
     * @return true if the user has the role, false otherwise
     */
    public static boolean hasRole(String role) {
        try {
            return getCurrentUser().hasRole(role);
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
