package unioeste.smch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import unioeste.smch.auth.JwtAuthenticationFailureHandler;
import unioeste.smch.auth.JwtAuthenticationFilter;
import unioeste.smch.auth.JwtAuthenticationSuccessHandler;
import unioeste.smch.auth.JwtUtil;


import javax.servlet.Filter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;



@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class Security extends WebSecurityConfigurerAdapter {

    private final JwtUtil jwtUtil;

    private final ObjectMapper objectMapper;

    private final UserDetailsService userDetailsService;

    @Autowired
    public Security(
            ObjectMapper objectMapper, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .formLogin().disable()
                .antMatcher("/api/**").authorizeRequests()
                .antMatchers(POST, "/api/login").permitAll()
                .antMatchers(POST, "/api/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(usernamePasswordAuthenticationFilter(), JwtAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Filter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public Filter usernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(jwtUtil, objectMapper));
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/api/login");
        return usernamePasswordAuthenticationFilter;
    }

}

