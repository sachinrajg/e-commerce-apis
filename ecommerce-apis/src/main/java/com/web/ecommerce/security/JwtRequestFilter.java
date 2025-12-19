package com.web.ecommerce.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import com.web.ecommerce.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/swagger-ui/") || 
            requestURI.startsWith("/v3/api-docs") || 
            requestURI.startsWith("/authenticate") || 
            requestURI.startsWith("/buyer/products") || 
            requestURI.startsWith("/register/buyer") || 
            requestURI.startsWith("/register/seller")) {
            
            logger.info("Bypassing JWT for request : {}", requestURI);
            chain.doFilter(request, response);
            return;
        }


        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwt);
                String username = claims.getSubject();

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(jwt, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        logger.info("Authenticated user: {}", username);
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired");
            } catch (Exception e) {
                logger.error("Error parsing JWT: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}