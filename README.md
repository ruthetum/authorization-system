# Authorization system
- 인증 시스템 만들기
- 2021.12.13. ~ 2021. 12.20.

### Goal
- MSA 활용 및 설계
- 프레임워크 제공 기술 최소 적용
- 요구사항 외 추가 기능 고민 및 구현

### Requirements
- 사용자 DB 설계
- 가입, 로그인 페이지
- 인증 서버 (API)
- RDBMS DB 사용
- Password Encryption
- 유저 관리 페이지 (Admin/BackOffice)
- E-Mail 인증
- 비밀번호 찾기
- 캐시 활용

### Architecture
![architecture](https://user-images.githubusercontent.com/59307414/146739853-f8531521-4ada-41be-b8de-94a8efba10c7.PNG)

### ERD
![erd](https://user-images.githubusercontent.com/59307414/146851663-0a41aba6-ff65-433f-b7df-544296b2c6ee.png)

### Stack
- Java11
- Springboot, Spring Clod Spring Data JPA
- Spring Cloud
- MySQL, MongoDB
- Redis
- Thymeleaf


<i>추가 자료는 [Wiki](https://github.com/ruthetum/authorization-system/wiki)</i>

### Reference
- [Spring Cloid Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/reference/html/)
- [Spring Data Redis](https://spring.io/projects/spring-data-redis)
- [Spring Data MongoDB Docs](https://spring.io/projects/spring-data-mongodb)
- [Spring-Mongo(baeldung)](https://www.baeldung.com/spring-data-mongodb-tutorial)