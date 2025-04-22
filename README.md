# 🧪 Team Reboott 프로젝트 실행 가이드

이 저장소는 AI Feature 사용량 및 크레딧 기반의 기능 제한 시스템을 기반으로 한 회사별 정책 관리 백엔드입니다.

## 🛠️ 개발 환경

- **Kotlin 1.9.x**
- **Spring Boot 3.4.x**
- **Gradle**
- **MySQL 8**
- **Redis**
- **Docker Compose (개발/테스트 환경용)**

---

## ⚙️ 로컬 실행 방법

### 1️⃣ Docker로 MySQL & Redis 실행

```bash
docker-compose up -d
```

### 2️⃣ DataBase 생성
```bash
docker exec -i mysql-team-reboott \
  mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS team_reboott_db; CREATE DATABASE IF NOT EXISTS test;"
```

### ℹ️ gradlew Permission denied가 발생할 경우 아래 명령어로 실행 권한을 부여하세요:

```bash
chmod +x ./gradlew
```

### 3️⃣ 애플리케이션 실행
```bash
./gradlew bootRun
```

### 4️⃣ 테스트 코드 실행
```bash
./gradlew test
```

### 5️⃣ Swagger로 API 문서 확인

[애플리케이션 실행 후 아래 URL에서 Swagger UI를 통해 API 명세를 확인할 수 있습니다:](http://localhost:8080/swagger-ui/index.html)