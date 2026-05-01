package com.study.auth;

import com.study.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/kakao")
  public ApiResponse<AuthDto.TokenResponse> kakaoLogin(@RequestBody AuthDto.OAuthRequest request) {
    return ApiResponse.ok(authService.kakaoLogin(request.getCode()));
  }

  @PostMapping("/google")
  public ApiResponse<AuthDto.TokenResponse> googleLogin(@RequestBody AuthDto.OAuthRequest request) {
    return ApiResponse.ok(authService.googleLogin(request.getCode()));
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
}
