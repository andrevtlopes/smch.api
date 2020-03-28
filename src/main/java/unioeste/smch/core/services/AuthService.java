package unioeste.smch.core.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

public class AuthService {

    public static boolean hasPermission(Principal user, String role) {
        Authentication authentication = (Authentication) user;
        return authentication.getAuthorities().stream().anyMatch(o -> o.getAuthority().equals(role));
    }

    public static boolean hasAnyPermission(Principal principal, String ... requiredRoles) {
        Authentication authentication = (Authentication) principal;

        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        for (String role : requiredRoles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }

        return false;
    }

}

