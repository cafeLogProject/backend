# backend

### 도커 실행(처음)
- docker-compose up --build

### 도커 실행(수정 사항 없을 때)
- docker-compose up

### 원하는 컨테이너만 재실행할때
- docker restart {name}

### 도커 실행 중지
- docker-compose down

### EC2에서 배포용으로 실행
- export $(grep -v '^#' .env.prod | xargs) && docker compose -f docker-compose-prod.yml up --build -d
- docker compose -f docker-compose-prod.yml down

### 맥 로컬에서 실행
- export $(grep -v '^#' .env | xargs) && docker-compose -f docker-compose-dev.yml up --build -d
- docker-compose -f docker-compose-dev.yml down

### 윈도우 로컬에서 실행
- .env.dev 파일명을 .env로 변경
- docker-compose -f docker-compose-dev.yml up --build