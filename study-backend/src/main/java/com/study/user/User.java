package com.study.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Provider provider;

  @Column(nullable = false, unique = true)
  private String providerId;

  private String email;
  private String nickname;

  @Enumerated(EnumType.STRING)
  private CharacterType characterType;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  public User(Provider provider, String providerId, String email, String nickname) {
    this.provider = provider;
    this.providerId = providerId;
    this.email = email;
    this.nickname = nickname;
    this.createdAt = LocalDateTime.now();
  }

  public void updateCharacterType(CharacterType characterType) {
    this.characterType = characterType;
  }
}
