package com.study.auth;

import lombok.Builder;
import lombok.Getter;

public class AuthDto {

  @Getter
  public static class OAuthRequest {
    private String code;
  }

  @Getter
  public static class RefreshRequest {
    private String refreshToken;
  }

  @Getter
  @Builder
  public static class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;
  }
}
