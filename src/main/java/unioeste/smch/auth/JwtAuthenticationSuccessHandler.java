package unioeste.smch.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private JwtUtil jwtUtil;
    private ObjectMapper objectMapper;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthenticatedUser authUser = (AuthenticatedUser) authentication.getPrincipal();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        JwtToken jwtToken = jwtUtil.createToken(authUser);
        response.getWriter().append(objectMapper.writeValueAsString(jwtToken));
    }
}

