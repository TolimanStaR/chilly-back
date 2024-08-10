package com.chilly.security_svc.config;

import com.chilly.security_svc.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String jwtToken = extractJwt(request);
        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String username;
        try {
            username = jwtService.extractUsername(jwtToken);
        }
        catch (Exception e) {
            handleResponse(response, e);
            return;
        }

        updateSecurityContextIfNecessary(request, jwtToken, username);
        filterChain.doFilter(request, response);
    }

    private String extractJwt(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    private void handleResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(e.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private void updateSecurityContextIfNecessary(HttpServletRequest request, String jwt, String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (username == null || authentication != null) {
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtService.isValidToken(jwt, userDetails)) {
            updateSecurityContextWithUserDetails(request, userDetails);
        }
    }

    private void updateSecurityContextWithUserDetails(HttpServletRequest request, UserDetails userDetails) {
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }


}
