package com.spring.mongodbPractice.security;

import com.spring.mongodbPractice.config.Constants;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        if(request.getServletPath().equals(Constants.LOGIN)){
            filterChain.doFilter(request,response);
            return;
        }else{
            String authHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);
            if(authHeader==null||!authHeader.startsWith(Constants.BEARER)){
                filterChain.doFilter(request,response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request,response);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(Constants.AUTHORIZATION_HEADER);
        if(token!=null){
            token = token.substring(Constants.BEARER.length());
            String user = Jwts
                    .parser()
                    .setSigningKey(Constants.SECURITY_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if(user!=null){
                return new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
