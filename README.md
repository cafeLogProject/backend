# ☕ brewscape - 카페로그
#### "공부하기 좋은 카페 없나? 커피가 맛있는 카페 없나?"
* 카페로그는 여러 장소 중에서도 카페 위주의 리뷰를 모아보고 싶은 사용자들을 위한 웹서비스입니다.
* 개개인의 취향에 따라 카페를 탐색하고 기록할 수 있습니다.

<br>

## 개발 기간
#### `2024.12.13 ~ 2025.2.6 (2개월)`

<br>

## 주요 기능
| **회원가입, 로그인, 마이페이지, AI챗봇** | **영수증 등록, 검색기능** |
|:---:|:---:|
| <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/23a1ad5b-0408-42e4-918a-c7c7fb7b0934" width="440" height="260" /> | <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/14686d48-6457-4f58-8576-043a581f158f" width="440" height="260" /> | 
| **음식점 상세페이지** | **회원 커뮤니티** |
| <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/adf1449a-b016-4a5c-874a-8c7839490ca7" width="440" height="260" /> | <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/eac545ae-84b0-4d6a-8d25-6ccd41ef3b17" width="440" height="260" /> | 
| **사장 커뮤니티** | **포인트 시스템, 랭킹 시스템** |
| <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/97064c73-b97d-417d-9e33-54ca1a7a96b5" width="440" height="260" /> | <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/3f370f44-47cb-480a-b433-5e885ff4f00d" width="440" height="260" /> | 
| **캘린더** | 
| <img src ="https://github.com/chujaeyeong/MAT_ZIP_readme_chujy/assets/123634960/1865cb93-c556-4752-8fb0-6547cf8e0c6b" width="440" height="260" /> 


<br>

## 어플리케이션 흐름
![](./image/flow.png)

<br>

## 기술 스택

### 프론트
| ![java](image/be/java.png) | ![maven](image/be/maven.png) | ![spring_boot](image/be/spring_boot.png) | ![jpa](image/be/jpa.png) | ![security](image/be/ss.png)  |
|----------------------------|------------------------------|------------------------------------------|--------------------------|-------------------------------|
| Java 17                    | maven                        | Spring Boot                              | Spring Data JPA          | Spring Security               |

### 백엔드 
| ![java](image/be/java.png) | ![maven](image/be/maven.png) | ![spring_boot](image/be/spring_boot.png) | ![jpa](image/be/jpa.png) | ![security](image/be/ss.png)  |
|----------------------------|------------------------------|------------------------------------------|--------------------------|-------------------------------|
| Java 17                    | maven                        | Spring Boot                              | Spring Data JPA          | Spring Security               |

| ![restdocs](image/be/restdocs.png) | ![junit](image/be/junit.png) | ![mockito](image/be/mockito.png) | ![mysql](image/be/mysql.png) |
|------------------------------------|------------------------------|----------------------------------|------------------------------|
| Spring RestDocs                    | Junit 5                      | Mockito                          | MySQL 8                      |
### DevOps
| ![actions](image/devops/actions.png) | ![ec2](image/devops/ec2.png) | ![s3](image/devops/s3.png) | ![codedeploy](image/devops/codedeploy.png) | ![rds](image/devops/rds.png) |
|--------------------------------------|------------------------------|----------------------------|--------------------------------------------|------------------------------|
| Github Actions                       | AWS EC2                      | AWS S3                     | AWS CodeDeploy                             | AWS RDS                      |

### 협업툴
| ![jira](image/work/jira.png) | ![notion](image/work/notion.png) | ![slack](image/work/slack.png) | ![github](image/work/github.png) |
|------------------------------|----------------------------------|--------------------------------|----------------------------------|
| JIRA                         | notion                           | Slack                          | Github                           |

<br>

## 주요 기능 구현
- [김병찬]
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

<br>

### ERD 설계

### 시스템 아키텍쳐 구성도

### 트러블 슈팅
[김병찬]
### `~~~한 문제 해결`
#### ❓ 문제가 뭐지? - 문제 정의
* 나나나

#### ❓ 왜 이런 문제가 발생하지? - 가설 세우기
* 나나나
* 나나ㅏ

#### 💡 이렇게 해서 해결! - 해결 방법 & 결과
* 나나
* 나나

#### 결론
* 나나
  
[문남경]
[이승헌]
[임용태]
[이주연]


## 프로젝트 문서
* [노션](https://www.notion.so/18fc5e41552d81bfa985c870e5c2fed4)
* [포스트맨](https://www.postman.com/cafelog/cafelog-team/overview)
* [피그마](https://www.figma.com/design/UPFpjUYoJa0nx1GiQNktp0/%5B2025-%ED%8C%80-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%5D-%EC%BB%A4%ED%94%BC-%EA%B8%B0%EB%A1%9D?node-id=0-1&t=0iUqRTZvxP7KlhpO-1)

