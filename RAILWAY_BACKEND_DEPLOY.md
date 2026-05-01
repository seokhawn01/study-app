# Railway Backend Deploy

## 설정 파일 역할

- `study-backend/src/main/resources/application.yml`
  - 모든 환경에서 공통으로 읽히는 기본 설정 파일.
  - 프로필이 활성화되면 `application-{profile}.yml`이 이 값을 덮어쓴다.
- `study-backend/src/main/resources/application-local.yml`
  - 로컬 개발용 설정.
- `study-backend/src/main/resources/application-prod.yml`
  - Railway 배포용 설정.
- `study-backend/src/main/resources/application-test.yml`
  - GitHub Actions와 로컬 테스트용 인메모리 DB 설정.

## 이번에 반영한 기준

- 기본 포트는 `PORT` 환경변수를 우선 사용한다.
- 기본 프로필은 `local`이다.
- 배포 시에는 반드시 `SPRING_PROFILES_ACTIVE=prod`를 사용한다.
- `application-prod.yml`은 Railway MySQL 변수(`MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQL_URL`)를 바로 읽을 수 있다.
- 기존 `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD`를 직접 넣는 방식도 유지한다.
- Railway 배포는 `study-backend/Dockerfile` 기준으로 진행할 수 있다.

## Railway에서 할 일

1. Railway 프로젝트 생성
2. GitHub 저장소 연결
3. 백엔드 서비스의 Root Directory를 `study-backend`로 설정
4. 같은 프로젝트에 MySQL 서비스 추가
5. 백엔드 서비스 Variables에 아래 값 추가

```env
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=...
KAKAO_CLIENT_ID=...
KAKAO_CLIENT_SECRET=...
KAKAO_REDIRECT_URI=https://your-domain.up.railway.app/api/auth/kakao/callback
GOOGLE_CLIENT_ID=...
GOOGLE_CLIENT_SECRET=...
GOOGLE_REDIRECT_URI=studyapp://auth/google
```

6. Railway Networking에서 public domain 생성
7. 배포 후 `https://your-domain.up.railway.app/api/health` 확인

## Dockerfile 기준 권장 설정

- Root Directory: `study-backend`
- Build Command: 비워두기
- Start Command: 비워두기
- `study-backend/Dockerfile`이 빌드와 실행을 담당

## GitHub Actions 역할

- `.github/workflows/backend-ci.yml`이 `study-backend` 변경 시 테스트를 수행한다.
- 실제 배포는 Railway의 GitHub 연동으로 `main` 반영 시 자동 배포하는 구성을 권장한다.

## 시크릿 원칙

- 실제 값은 `.env` 또는 Railway Variables, GitHub Secrets에만 둔다.
- `.env.example`에는 키 이름만 둔다.
- 현재 로컬 `.env`에 있는 실제 OAuth 값은 유지하되 Git에 올리지 않는다.

## FastAPI 연결

- 현재 저장소에는 FastAPI 호출 코드가 없다.
- 연결 자체는 가능하다.
- 권장 방식은 Railway에 FastAPI를 별도 서비스로 띄우고, Spring Boot가 환경변수 기반 URL로 HTTP 호출하는 구조다.
- 이 경우 `FASTAPI_BASE_URL` 같은 변수를 추가하고, 백엔드에 호출용 client와 예외 처리를 넣으면 된다.
