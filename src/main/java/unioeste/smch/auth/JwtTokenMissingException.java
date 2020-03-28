package unioeste.smch.auth;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {
    public JwtTokenMissingException(String s) {
        super(s);
    }
}
