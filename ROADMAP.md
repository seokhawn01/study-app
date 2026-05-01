# ROADMAP

## 기술 스택

### 프론트엔드
- React Native + Expo (Expo Router)
- NativeWind v4 (Tailwind CSS for React Native)
- TypeScript
- Zustand (클라이언트 상태), TanStack Query (서버 상태)
- expo-auth-session (OAuth)

### 백엔드
- Java 17 + Spring Boot
- JWT 인증 (JJWT, Spring Security 없이 필터만 사용)
- Spring Data JPA + MySQL Driver
- Lombok

### 배포
- Railway: Spring Boot 백엔드
- Railway MySQL: 데이터베이스
- GitHub Actions: CI/CD

### 로그인
- 카카오 (expo-auth-session + REST API)
- 구글 이메일 (expo-auth-session + OAuth)

---

## DB 스키마

```sql
-- 유저
users (
  id           BIGINT PK AUTO_INCREMENT,
  provider     ENUM('KAKAO','GOOGLE') NOT NULL,
  provider_id  VARCHAR(100) NOT NULL UNIQUE,
  email        VARCHAR(100),
  nickname     VARCHAR(50),
  character_type ENUM('OWL','TURTLE','LION','CAT','FOX') NULL,
  created_at   DATETIME
)

-- 리프레시 토큰
refresh_tokens (
  id           BIGINT PK,
  user_id      BIGINT FK → users,
  token        VARCHAR(500),
  expires_at   DATETIME
)

-- 성장 정보
user_growth (
  user_id      BIGINT PK FK → users,
  level        INT DEFAULT 1,
  exp          INT DEFAULT 0
)

-- 공부 세션
study_sessions (
  id           BIGINT PK,
  user_id      BIGINT FK → users,
  started_at   DATETIME,
  ended_at     DATETIME,
  duration_minutes INT
)

-- 미션 정의 (사전 데이터)
missions (
  id           BIGINT PK,
  character_type ENUM('OWL','TURTLE','LION','CAT','FOX'),
  title        VARCHAR(100),
  description  TEXT,
  exp_reward   INT,
  mission_type ENUM('DAILY','ONCE')
)

-- 유저 미션 완료 기록
user_missions (
  id           BIGINT PK,
  user_id      BIGINT FK → users,
  mission_id   BIGINT FK → missions,
  completed_at DATE
)

-- 아이템 정의
items (
  id           BIGINT PK,
  type         ENUM('CHARACTER','ROOM'),
  slot         VARCHAR(50),
  name         VARCHAR(100),
  image_key    VARCHAR(200),
  unlock_level INT,
  character_type ENUM('OWL','TURTLE','LION','CAT','FOX') NULL
)

-- 유저 보유 아이템
user_items (
  id           BIGINT PK,
  user_id      BIGINT FK → users,
  item_id      BIGINT FK → items,
  obtained_at  DATETIME
)

-- 현재 장착 상태
user_equip (
  user_id      BIGINT FK → users,
  slot         VARCHAR(50),
  item_id      BIGINT FK → items,
  PRIMARY KEY (user_id, slot)
)
```

---

## API 엔드포인트

모든 응답 형식: `{ success: boolean, data: T, message?: string }`

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | /api/auth/kakao | 카카오 로그인 → JWT 발급 |
| GET | /api/auth/kakao/callback | 카카오 OAuth 콜백 → JWT 발급 후 앱 딥링크로 리다이렉트 |
| POST | /api/auth/google | 구글 로그인 → JWT 발급 |
| GET | /api/auth/google/callback | 구글 OAuth 콜백 → JWT 발급 후 앱 딥링크로 리다이렉트 |
| POST | /api/auth/refresh | 액세스 토큰 재발급 |
| DELETE | /api/auth/logout | 로그아웃 |
| GET | /api/users/me | 내 정보 조회 |
| PUT | /api/users/character | 캐릭터 타입 저장 |
| POST | /api/test/result | 테스트 결과 저장 + 첫 미션 반환 |
| GET | /api/home | 홈 화면 통합 데이터 |
| POST | /api/study/start | 공부 세션 시작 |
| POST | /api/study/complete | 공부 세션 완료 |
| GET | /api/missions/today | 오늘의 미션 목록 |
| POST | /api/missions/{id}/complete | 미션 완료 처리 |
| GET | /api/growth | 성장 정보 조회 |
| GET | /api/items | 보유/전체 아이템 목록 |
| GET | /api/customize | 현재 장착 상태 |
| PUT | /api/customize | 아이템 장착 변경 |

---

## 캐릭터 타입

| 코드 | 이름 | 특징 |
|------|------|------|
| OWL | 올빼미형 | 밤에 집중, 야간 공부 선호 |
| TURTLE | 거북이형 | 꾸준함, 매일 조금씩 |
| LION | 사자형 | 도전적, 목표 중심 |
| CAT | 고양이형 | 감성적, 분위기 중시 |
| FOX | 여우형 | 계획적, 체계 우선 |

---

## 레벨 경험치 기준

| 레벨 | 필요 누적 경험치 |
|------|----------------|
| Lv.1 → 2 | 100 xp |
| Lv.2 → 3 | 300 xp |
| Lv.3 → 4 | 600 xp |
| Lv.4 → 5 | 1,000 xp |

---

## MVP 구현 순서

### Phase 0 — 프로젝트 초기 세팅
**프론트** (`study-app/`)
- [x] `npx create-expo-app study-app --template blank-typescript`
- [x] NativeWind v4 설치 및 설정
- [x] Expo Router 설정
- [x] Zustand + TanStack Query 설치
- [x] 폴더 구조 생성 (app, components, hooks, lib, store, types, constants)
- [x] 기본 네비게이션 뼈대 ((auth)/(main) 그룹)

**백엔드** (`study-backend/`)
- [x] Spring Initializr로 프로젝트 생성
- [x] 의존성: Spring Web, JPA, MySQL Driver, Lombok, JJWT
- [x] application.yml (local/prod 프로파일 분리)
- [x] 공통 응답 `ApiResponse<T>` 클래스
- [x] `/api/health` 엔드포인트
- [x] CORS 설정

검증: 앱이 Expo Go에서 실행 + `/api/health` 200 OK

---

### Phase 1 — 인증 시스템

**백엔드**
- [x] JWT 유틸리티 (발급/검증)
- [x] JWT 필터 (요청마다 토큰 검증)
- [x] User 엔티티 + Repository
- [x] 카카오 OAuth 처리 서비스
- [x] 구글 OAuth 처리 서비스
- [x] AuthController (POST /api/auth/kakao, /google, /refresh, DELETE /logout)

**프론트**
- [x] 로그인 화면 UI
- [x] expo-auth-session 카카오 연동 (WebBrowser.openAuthSessionAsync + 백엔드 콜백)
- [x] expo-auth-session 구글 연동 (코드 완성, Google credentials 발급 후 테스트 필요)
- [x] AccessToken 저장 (SecureStore)
- [x] API 클라이언트 (axios + 인터셉터)
- [x] 인증 상태에 따른 라우팅 처리

검증: 카카오 로그인 → JWT 발급 → `/api/users/me` 200 OK
> 미완료: Expo 서버 실행 후 카카오 실기기 테스트 필요. 구글은 Google Cloud Console credentials 발급 후 application-local.yml에 client_id/secret 추가 필요.

---

### Phase 2 — 온보딩 + 공부 유형 테스트 + 결과 화면

**프론트**
- [x] 온보딩 화면 (서비스 소개 슬라이드)
- [x] AsyncStorage로 온보딩 완료 여부 저장
- [ ] 테스트 화면 (질문 카드 UI, 10문항, 프론트 하드코딩)
- [ ] 유형 계산 로직 (캐릭터별 가중치 합산)
- [ ] 결과 화면 (캐릭터, 유형명, 추천 공부법, 첫 미션)

**백엔드**
- [ ] Character 관련 데이터 반환 API
- [x] `PUT /api/users/character`로 캐릭터 타입 저장
- [ ] `POST /api/test/result`로 첫 미션 반환

검증: 테스트 완료 → 결과 화면 → DB character_type 저장 확인

---

### Phase 3 — 홈 화면

**백엔드**
- [ ] StudySession 엔티티 + Repository
- [ ] Mission 엔티티 + 초기 데이터 (캐릭터별 미션 SQL)
- [ ] 오늘의 미션 조회 (당일 완료 여부 포함)
- [ ] 공부 시작/완료 API (경험치 부여)
- [ ] 연속 공부일(streak) 계산 로직
- [ ] `GET /api/home` 통합 데이터 API

**프론트**
- [ ] 홈 화면 UI (캐릭터, 미션 카드, 타이머, streak)
- [ ] 공부 타이머 구현
- [ ] 미션 완료 처리
- [ ] TanStack Query로 홈 데이터 캐싱

검증: 공부 완료 → 경험치 증가 → 미션 완료 → 홈 갱신

---

### Phase 4 — 성장 시스템 + 꾸미기 화면

**백엔드**
- [ ] UserGrowth 엔티티
- [ ] 경험치 누적 + 레벨업 로직
- [ ] Item 엔티티 + 초기 아이템 데이터
- [ ] 레벨업 시 아이템 해금 처리
- [ ] UserEquip 엔티티
- [ ] 꾸미기 조회/수정 API

**프론트**
- [ ] 성장 화면 (레벨, 경험치 바)
- [ ] 레벨업 애니메이션
- [ ] 꾸미기 화면 (캐릭터/방 탭)
- [ ] 아이템 그리드 (보유/미보유 구분)
- [ ] 장착 미리보기

검증: Lv1→2 달성 → 아이템 해금 → 꾸미기에서 장착 확인

---

### Phase 5 — 배포

- [ ] Railway MySQL 설정 + 스키마 마이그레이션
- [ ] Spring Boot application-prod.yml 환경변수 연결
- [ ] Railway 백엔드 배포
- [ ] GitHub Actions 워크플로우 (main push → 빌드 → 배포)
- [ ] 프론트 .env.production 설정 (API 베이스 URL)

검증: main push → 자동 배포 → Railway URL API 호출 성공
