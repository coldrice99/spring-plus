# SPRING PLUS

---

## Level.1: 코드 개선 퀴즈 - @Transactional의 이해

### 에러 원인
`TodoService` 클래스에 `@Transactional(readOnly = true)`가 설정되어 있어, **일정 생성 시 데이터베이스가 읽기 전용 모드로 연결**되었습니다. 이로 인해 데이터 수정 작업(`INSERT`)이 차단되면서 에러가 발생했습니다.

### 해결 방법
`saveTodo` 메서드에 `@Transactional`을 추가하여, **해당 메서드만 읽기-쓰기 모드로 설정**했습니다.

---

## Level.2: 코드 추가 퀴즈 - JWT의 이해

### 해결 방법
1. **User 엔티티에 `nickname` 필드 추가**: `User` 엔티티에 `nickname` 필드를 추가하여 닉네임 정보를 저장할 수 있도록 수정했습니다.
2. **DTO 및 생성자에 `nickname` 필드 추가**: 회원가입, 로그인, 일정 생성 등의 DTO와 생성자에 `nickname` 필드를 추가하여 닉네임 정보를 포함했습니다.
3. **JWT 필터 수정**: `JwtFilter`에서 `httpRequest.setAttribute("nickname", claims.get("nickname"))`를 추가하여, JWT에서 추출한 `nickname` 값이 요청 속성으로 전달되도록 설정했습니다.

---

## Level.3: 코드 개선 퀴즈 - AOP의 이해

### 해결 방법
1. **`@Before` 어노테이션 사용**: AOP가 `changeUserRole()` 메서드 실행 **전**에 동작하도록 `@After`를 `@Before`로 변경했습니다.
2. **포인트컷 수정**: 포인트컷을 `"execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))"`로 지정하여, **`UserAdminController`의 `changeUserRole()` 메서드**에 AOP가 적용되도록 수정했습니다.

맞습니다, 혼동을 드려서 죄송합니다! 예외 처리기를 수정하지 않고 **테스트 코드만 수정**해서 해결한 경우라면 아래와 같이 간단하게 작성할 수 있습니다.


