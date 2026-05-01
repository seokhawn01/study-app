# study-backend

Java 21 + Spring Boot 3.5 백엔드. Spring Data JPA + MySQL, JWT 필터 기반 인증(Spring Security 미사용).

## 구조
- `auth/` — JWT 발급/검증, 카카오·구글 OAuth, 리프레시 토큰
- `user/` — 유저 엔티티, 캐릭터 타입 저장
- `common/` — `ApiResponse<T>`, CORS, 전역 예외처리, 헬스체크

## 실행
```bash
JAVA_HOME="/c/Program Files/Java/jdk-21" ./gradlew bootRun
```
