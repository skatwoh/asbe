package org.example.asbe.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DebugFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("DEBUG FILTER: Request URI -> " + request.getRequestURI());
        System.out.println("DEBUG FILTER: Authorization Header -> " + request.getHeader("Authorization"));

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("DEBUG FILTER: Cookie -> " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("DEBUG FILTER: Không có cookie nào.");
        }

        filterChain.doFilter(request, response);
    }
}
