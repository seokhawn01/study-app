package com.study.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserDto {

  @Getter
  @Builder
  public static class Response {
    private Long id;
    private String email;
    private String nickname;
    private CharacterType characterType;
    private LocalDateTime createdAt;

    public static Response from(User user) {
      return Response.builder()
          .id(user.getId())
          .email(user.getEmail())
          .nickname(user.getNickname())
          .characterType(user.getCharacterType())
          .createdAt(user.getCreatedAt())
          .build();
    }
  }

  @Getter
  public static class CharacterRequest {
    private CharacterType characterType;
  }
}
