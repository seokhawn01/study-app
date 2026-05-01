package com.study.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private static final List<String> PERMIT_PATHS = List.of(
      "/api/health",
      "/api/auth/kakao",
      "/api/auth/google",
      "/api/auth/refresh"
  );

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String path = request.getRequestURI();
    if (PERMIT_PATHS.stream().anyMatch(path::startsWith)) {
      chain.doFilter(request, response);
      return;
    }

    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다.");
      return;
    }

    String token = header.substring(7);
    if (!jwtUtil.isValid(token)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
      return;
    }

    request.setAttribute("userId", jwtUtil.getUserId(token));
    chain.doFilter(request, response);
  }
}
