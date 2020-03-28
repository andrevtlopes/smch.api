package unioeste.smch.auth;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {
    public JwtTokenMalformedException(String s) {
        super(s);
    }
}
