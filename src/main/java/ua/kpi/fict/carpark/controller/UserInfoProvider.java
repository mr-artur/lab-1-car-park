package ua.kpi.fict.carpark.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ua.kpi.fict.carpark.entity.enums.Role;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoProvider {

    public static boolean isSuperAdmin() {
        return hasRole(Role.ROLE_SUPERADMIN);
    }

    public static boolean isAdmin() {
        return hasRole(Role.ROLE_ADMIN);
    }

    public static boolean isAuthenticated() {
        return !getAuthentication().getName().equals("anonymousUser");
    }

    private static UserDetails getAuthenticatedUser() {
        return (UserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private static boolean hasRole(Role role) {
        return getAuthenticatedUser().getAuthorities().contains(role);
    }

    public static String getUsername() {
        return getAuthenticatedUser().getUsername();
    }
}
