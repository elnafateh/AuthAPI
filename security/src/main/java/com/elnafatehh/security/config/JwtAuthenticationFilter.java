package com.elnafatehh.security.config;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
         @NotNull HttpServletRequest request,
         @NotNull HttpServletResponse response,
         @NotNull FilterChain filterChain)

            throws ServletException, IOException {
            final String authHeader= request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            //check Jwt token;
            if(authHeader==null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            //extract token from authHeader
            jwt = authHeader.substring(7);
            // todo extract the userEmail from JWT token;
            userEmail=jwtService.extractUsername(jwt);
            if (userEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails =this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken  authToken =new UsernamePasswordAuthenticationToken(
                           userDetails,
                            null,
                            userDetails.getAuthorities()

                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);
    }
    }
