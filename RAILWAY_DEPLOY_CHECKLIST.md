# Railway Deploy Checklist

## 1. GitHub 정리

- [ ] `.env`가 커밋되지 않았는지 확인
- [ ] `.env.example`만 저장소에 유지
- [ ] `main` 브랜치 기준으로 배포할 상태 정리

## 2. Railway 프로젝트 생성

- [ ] Railway에서 새 Project 생성
- [ ] `Deploy from GitHub repo` 선택
- [ ] 현재 GitHub 저장소 연결

## 3. 백엔드 서비스 설정

- [ ] 생성된 서비스의 Root Directory를 `study-backend`로 설정
- [ ] `study-backend/Dockerfile`을 사용하도록 Build Command / Start Command를 비워두기
- [ ] Public Domain 생성
- [ ] Deploy Logs에서 Spring Boot 기동 확인

## 4. Railway MySQL 추가

- [ ] 같은 Project 안에 MySQL 서비스 추가
- [ ] MySQL 서비스가 `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQL_URL`를 제공하는지 확인

## 5. 백엔드 Variables 복붙

백엔드 서비스 Variables에 아래를 추가:

```env
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=여기에_충분히_긴_랜덤_시크릿
KAKAO_CLIENT_ID=여기에_카카오_클라이언트_ID
KAKAO_CLIENT_SECRET=여기에_카카오_클라이언트_SECRET
KAKAO_REDIRECT_URI=https://백엔드_도메인.up.railway.app/api/auth/kakao/callback
GOOGLE_CLIENT_ID=여기에_구글_클라이언트_ID
GOOGLE_CLIENT_SECRET=여기에_구글_클라이언트_SECRET
GOOGLE_REDIRECT_URI=https://your-backend-domain.up.railway.app/api/auth/google/callback
```

선택:

```env
DATABASE_URL=
DB_USERNAME=
DB_PASSWORD=
```

- Railway MySQL 기본 변수를 그대로 쓸 거면 위 3개는 비워둬도 됨
- 외부 MySQL을 쓸 때만 `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD`를 직접 넣으면 됨

## 6. OAuth 콘솔 수정

- [ ] 카카오 개발자 콘솔에 운영 Redirect URI 추가
- [ ] 구글 콘솔에도 운영 환경 기준 값 확인
- [ ] 로컬 `localhost` 값과 운영 Railway 도메인을 혼동하지 않기

카카오 운영 Redirect URI 예시:

```text
https://백엔드_도메인.up.railway.app/api/auth/kakao/callback
```

## 7. 배포 검증

- [ ] `https://백엔드_도메인.up.railway.app/api/health` 호출
- [ ] 회원가입/로그인 기본 플로우 확인
- [ ] DB에 사용자 데이터 저장 확인
- [ ] Railway 로그에서 datasource 연결 성공 확인

## 8. 프론트 연결

- [ ] 프론트 환경변수에 백엔드 주소 반영

```env
EXPO_PUBLIC_API_URL=https://백엔드_도메인.up.railway.app
```

## 9. GitHub Actions 확인

- [ ] GitHub Actions에서 `Backend CI` 워크플로 실행 확인
- [ ] PR 생성 시 테스트 통과 확인
- [ ] `main` 반영 후 Railway 자동 배포 확인

## 10. FastAPI를 붙일 경우

가능하다. 다만 이 저장소에는 아직 FastAPI 호출 코드가 없다.

권장 구조:

- Railway에 Spring Boot 서비스 1개
- Railway에 FastAPI 서비스 1개
- Spring Boot가 `FASTAPI_BASE_URL`로 FastAPI를 HTTP 호출

추가될 변수 예시:

```env
FASTAPI_BASE_URL=https://your-fastapi-service.up.railway.app
FASTAPI_API_KEY=optional-secret
```

적용 시 해야 할 일:

- [ ] FastAPI 서비스 배포
- [ ] Spring Boot에 FastAPI 호출용 client 추가
- [ ] `application-prod.yml` 또는 공통 설정에 `fastapi.base-url` 추가
- [ ] 호출 실패 시 timeout / fallback 정책 정리
