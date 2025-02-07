## 소개

### 프로젝트 개요

- 개개인의 취향에 따라 카페를 탐색하고 기록할 수 있는 웹서비스

### 개발 기간

- 2024.12.13 ~ 2025.2.6 (2달간)

### 주요 기능

- 카페 검색 기능
  - 카페명으로 검색
  - 북마크한 카페 검색
    
- 카페 리뷰 작성 기능
  - 사진, 별점, 카테고리가 포함된 리뷰 작성
  - 작성 도중 화면 이탈시 임시저장

- 리뷰 탐색 기능
  - 별점순, 최근순으로 나열된 리뷰 탐색
  - 카테고리로 리뷰 검색 (공부하기 좋은 곳, 커피의 맛, 인테리어, 데이트 추천 등)

- 특정 카페의 리뷰 탐색 기능

### 주요 기능 구현
- [김병찬] ㅋㅋㅋㅋㅋ
  - 로그인/회원가입
    - oauth2를 이용
    - access, refresh 토큰을 이용하여
  - 카페 조회
    - 네이버 API를 이용한 카페 정보 최신화
- [이주연]
    - 이미지 관련 기능
      - 이미지 저장시 서버 내부에 저장
    - 리뷰 임시저장 기능
        - 임시저장 개별 테이블 생성하여 관리
    - 리뷰 필터링 조회
      - SQL 쿼리를 사용한 필터링
      
    - 
    - 기능 테스트
      - 통합 테스트 코드 작성
## 기술 스택

### 핵심 기술

- **Framework**: React 18
- **Language**: TypeScript
- **Build Tool**: Vite
- **State Management**:
    - Zustand (전역 상태 관리)
    - TanStack Query v5 (서버 상태 관리)

### 스타일링

- SCSS Modules
    - 컴포넌트별 스코프된 스타일링
    - `*.module.scss` 파일 사용
    - 7-1 pattern 사용
    - bem 방법론 적용

### 테스트 및 개발 도구

- MSW (API Mocking)
- Vitest
- React Testing Library

### 인증

- OAuth2.0
    - Google
    - Facebook
    - Naver

### 주요 라이브러리

- `axios`: HTTP 클라이언트
- `js-cookie`: 쿠키 관리
- `react-router-dom`: 라우팅
- `@tanstack/react-query`: 서버 상태 관리
- `zustand`: 전역 상태 관리
- `msw`: API 모킹
- `swiper, react-modal, react-toastify` : UI/UX

### 코드 품질

- ESLint
- Prettier
- TypeScript strict 모드

### 아키텍처

- Feature-Sliced Design (FSD)
    - **app/**: 앱 실행 관련 설정 (라우팅, 진입점, 전역 스타일, 프로바이더).
    - **pages/**: 주요 페이지 및 중첩 라우팅 구성.
    - **widgets/**: 독립적으로 동작하는 대형 UI 또는 기능 컴포넌트.
    - **features/**: 재사용 가능한 비즈니스 기능 모듈.
    - **entities/**: 프로젝트의 핵심 비즈니스 엔티티
    - **shared/**: 범용적으로 재사용 가능한 기능 (비즈니스 로직과 분리될 수도 있음).
