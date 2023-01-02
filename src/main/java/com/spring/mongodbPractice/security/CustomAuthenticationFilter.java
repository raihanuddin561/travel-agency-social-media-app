package com.spring.mongodbPractice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mongodbPractice.SpringApplicationContext;
import com.spring.mongodbPractice.config.Constants;
import com.spring.mongodbPractice.dto.UserResponseModel;
import com.spring.mongodbPractice.service.UserService;
import com.spring.mongodbPractice.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);
            username = requestMap.get("email");
            password = requestMap.get("password");
            log.info("Login with " + username);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            catchActionForAuthenticationException(response, e, HttpServletResponse.SC_BAD_REQUEST);
            throw new RuntimeException(String.format("Error in attemptAuthentication with username: %s", username));
        } catch (Exception e) {
            catchActionForAuthenticationException(response, e, HttpServletResponse.SC_BAD_REQUEST);
            throw new RuntimeException(String.format("Error in attemptAuthentication with username: %s", username));
        }
    }

    private void catchActionForAuthenticationException(HttpServletResponse response,
                                                       Exception e, int scBadRequest) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("errorMessage", e.getMessage());
        response.setContentType("application/json");
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        String token = Util.getToken(username);
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        UserResponseModel user = userService.getUserByEmail(username);
        response.setHeader(Constants.AUTHORIZATION_HEADER, Constants.BEARER + token);
        response.setHeader("UserID", user.getId());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("errorMessage", "Bad credentials");
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);
    }
}
