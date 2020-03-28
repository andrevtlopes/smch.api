package unioeste.smch.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO: analisar se é util o rememberMeService conforme é utilizado no BasicAuthenticationFilter
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService) {
        this(jwtUtil, userDetailsService, new JwtAuthenticationEntryPoint());
    }

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(jwtUtil, "A JwtUtil is required");
        Assert.notNull(userDetailsService, "An UserDetailsService is required");
        Assert.notNull(authenticationEntryPoint, "An AuthenticationEntryPoint is required");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            String token = extractTokenFromHeader(request);
            String username = getUsernameFromToken(token);
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetailsService.loadUserByUsername(username);

            if (authenticatedUser.isDisabled()) {
                throw new BadCredentialsException("User is disabled");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authenticatedUser.getUsername(), authenticatedUser.getPassword(), authenticatedUser.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            updateTokenHeader(response, authenticatedUser);
        } catch (JwtTokenMissingException e) {
            LOG.error("Authentication Failure: {}", e.getMessage());
        } catch (AuthenticationException e) {
            LOG.error("Authentication Failure: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
            return;
        }

        chain.doFilter(request, response);
    }

    private void updateTokenHeader(HttpServletResponse response, AuthenticatedUser authenticatedUser) {
        response.setHeader("X-AUTH-TOKEN", jwtUtil.createToken(authenticatedUser).getToken());
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }
        return header.substring(7);
    }

    private String getUsernameFromToken(String token) {
        try {
            return jwtUtil.extractUsernameFromToken(token);
        } catch (MalformedJwtException e) {
            throw new JwtTokenMalformedException("Malformed JWT token");
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Expired JWT Token");
        }
    }

}

