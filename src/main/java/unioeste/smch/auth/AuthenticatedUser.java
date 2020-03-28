package unioeste.smch.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedUser implements UserDetails {

    private String username;

    private String email;

    private String password;

    private String name;

    private boolean enabled;

    private List<String> roles;

    public AuthenticatedUser(String name, String username, String email, String password, boolean enabled, List<String> roles) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isDisabled() {
        return !enabled;
    }

    public List<String> getRoles() {
        return roles;
    }
}

