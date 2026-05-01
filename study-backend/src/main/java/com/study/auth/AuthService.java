package com.study.auth;

import com.study.user.Provider;
import com.study.user.User;
import com.study.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;

  @Value("${oauth.kakao.client-id:}")
  private String kakaoClientId;

  @Value("${oauth.kakao.redirect-uri:}")
  private String kakaoRedirectUri;

  @Value("${oauth.kakao.client-secret:}")
  private String kakaoClientSecret;

  @Value("${oauth.google.client-id:}")
  private String googleClientId;

  @Value("${oauth.google.client-secret:}")
  private String googleClientSecret;

  @Value("${oauth.google.redirect-uri:}")
  private String googleRedirectUri;

  @Transactional
  public AuthDto.TokenResponse kakaoLogin(String code) {
    String kakaoAccessToken = getKakaoAccessToken(code);
    Map<String, Object> userInfo = getKakaoUserInfo(kakaoAccessToken);

    String providerId = String.valueOf(userInfo.get("id"));
    @SuppressWarnings("unchecked")
    Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
    String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    @SuppressWarnings("unchecked")
    Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
    String nickname = profile != null ? (String) profile.get("nickname") : "카카오유저";

    return processLogin(Provider.KAKAO, providerId, email, nickname);
  }

  @Transactional
  public AuthDto.TokenResponse googleLogin(String code) {
    String googleAccessToken = getGoogleAccessToken(code);
    Map<String, Object> userInfo = getGoogleUserInfo(googleAccessToken);

    String providerId = (String) userInfo.get("sub");
    String email = (String) userInfo.get("email");
    String nickname = (String) userInfo.get("name");

    return processLogin(Provider.GOOGLE, providerId, email, nickname);
  }

  @Transactional
  public AuthDto.TokenResponse refresh(String refreshTokenValue) {
    RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

    if (refreshToken.isExpired()) {
      refreshTokenRepository.delete(refreshToken);
      throw new IllegalArgumentException("만료된 리프레시 토큰입니다.");
    }

    String newAccessToken = jwtUtil.createAccessToken(refreshToken.getUserId());
    return AuthDto.TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(refreshTokenValue)
        .isNewUser(false)
        .build();
  }

  @Transactional
  public void logout(Long userId) {
    refreshTokenRepository.deleteByUserId(userId);
  }

  private AuthDto.TokenResponse processLogin(Provider provider, String providerId, String email, String nickname) {
    boolean isNewUser = !userRepository.findByProviderAndProviderId(provider, providerId).isPresent();

    User user = userRepository.findByProviderAndProviderId(provider, providerId)
        .orElseGet(() -> userRepository.save(
            User.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .nickname(nickname)
                .build()
        ));

    String accessToken = jwtUtil.createAccessToken(user.getId());
    String refreshTokenValue = jwtUtil.createRefreshToken(user.getId());

    refreshTokenRepository.deleteByUserId(user.getId());
    refreshTokenRepository.save(
        RefreshToken.builder()
            .userId(user.getId())
            .token(refreshTokenValue)
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build()
    );

    return AuthDto.TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshTokenValue)
        .isNewUser(isNewUser)
        .build();
  }

  @SuppressWarnings("unchecked")
  private String getKakaoAccessToken(String code) {
    RestClient client = RestClient.create();
    Map<String, Object> response = client.post()
        .uri("https://kauth.kakao.com/oauth/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body("grant_type=authorization_code"
            + "&client_id=" + kakaoClientId
            + (kakaoClientSecret == null || kakaoClientSecret.isBlank() ? "" : "&client_secret=" + kakaoClientSecret)
            + "&redirect_uri=" + kakaoRedirectUri
            + "&code=" + code)
        .retrieve()
        .body(Map.class);
    return (String) response.get("access_token");
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getKakaoUserInfo(String accessToken) {
    RestClient client = RestClient.create();
    return client.get()
        .uri("https://kapi.kakao.com/v2/user/me")
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .body(Map.class);
  }

  @SuppressWarnings("unchecked")
  private String getGoogleAccessToken(String code) {
    RestClient client = RestClient.create();
    Map<String, Object> response = client.post()
        .uri("https://oauth2.googleapis.com/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body("grant_type=authorization_code"
            + "&client_id=" + googleClientId
            + "&client_secret=" + googleClientSecret
            + "&redirect_uri=" + googleRedirectUri
            + "&code=" + code)
        .retrieve()
        .body(Map.class);
    return (String) response.get("access_token");
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getGoogleUserInfo(String accessToken) {
    RestClient client = RestClient.create();
    return client.get()
        .uri("https://www.googleapis.com/oauth2/v3/userinfo")
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .body(Map.class);
  }
}
