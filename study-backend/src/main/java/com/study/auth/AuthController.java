package com.study.auth;

import com.study.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Value("${app.frontend-redirect-uri:studyapp://auth/callback}")
  private String frontendRedirectUri;

  @PostMapping("/kakao")
  public ApiResponse<AuthDto.TokenResponse> kakaoLogin(@RequestBody AuthDto.OAuthRequest request) {
    return ApiResponse.ok(authService.kakaoLogin(request.getCode()));
  }

  @GetMapping("/kakao/callback")
  public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
    AuthDto.TokenResponse token = authService.kakaoLogin(code);
    response.sendRedirect(buildRedirectUrl(token));
  }

  @PostMapping("/google")
  public ApiResponse<AuthDto.TokenResponse> googleLogin(@RequestBody AuthDto.OAuthRequest request) {
    return ApiResponse.ok(authService.googleLogin(request.getCode()));
  }

  @GetMapping("/google/callback")
  public void googleCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
    AuthDto.TokenResponse token = authService.googleLogin(code);
    response.sendRedirect(buildRedirectUrl(token));
  }

  @PostMapping("/refresh")
  public ApiResponse<AuthDto.TokenResponse> refresh(@RequestBody AuthDto.RefreshRequest request) {
    return ApiResponse.ok(authService.refresh(request.getRefreshToken()));
  }

  @DeleteMapping("/logout")
  public ApiResponse<Void> logout(@RequestAttribute Long userId) {
    authService.logout(userId);
    return ApiResponse.ok(null);
  }

  private String buildRedirectUrl(AuthDto.TokenResponse token) {
    return frontendRedirectUri
        + "?accessToken=" + token.getAccessToken()
        + "&refreshToken=" + token.getRefreshToken()
        + "&isNewUser=" + token.isNewUser();
  }
}
