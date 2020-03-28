package unioeste.smch.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private String secret;

    private long expires;

    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiresInSeconds}") long expires) {
        this.secret = secret;
        this.expires = expires;
    }

    public String extractUsernameFromToken(String token) {
        Claims body = parseToken(token);
        return body.getSubject();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtToken createToken(AuthenticatedUser authenticatedUser) {
        Claims claims = Jwts.claims().setSubject(authenticatedUser.getUsername());
        claims.put("name", authenticatedUser.getName());
        claims.put("email", authenticatedUser.getEmail());
        claims.put("username", authenticatedUser.getUsername());
        claims.put("roles", authenticatedUser.getRoles());

        Instant expirationTime = Instant.now()
                .plusSeconds(expires);

        Date expirationDate = Date.from(expirationTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .setExpiration(expirationDate)
                .compact();

        return new JwtToken(token);
    }
}

