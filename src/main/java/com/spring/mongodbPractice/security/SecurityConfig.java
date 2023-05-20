package com.spring.mongodbPractice.security;

import com.spring.mongodbPractice.config.Constants;
import com.spring.mongodbPractice.security.CustomAuthenticationFilter;
import com.spring.mongodbPractice.utils.JwtUtils;
import com.spring.mongodbPractice.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userService;
    private final LogUtils logUtils;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests((auth) -> {
                    auth.antMatchers(HttpMethod.POST, Constants.LOGIN).permitAll()
                            .antMatchers(HttpMethod.POST, Constants.API_USER).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilter(new CustomAuthenticationFilter(authenticationManager,jwtUtils,userService))
                .addFilterBefore(new CustomAuthorizationFilter(jwtUtils,userService,logUtils), UsernamePasswordAuthenticationFilter.class)
                .headers()
                .cacheControl();
        return http.build();
    }
}
