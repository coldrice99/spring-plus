# SPRING PLUS

---

## Level.1: 코드 개선 퀴즈 - @Transactional의 이해

### 에러 원인
`TodoService` 클래스에 `@Transactional(readOnly = true)`가 설정되어 있어, **일정 생성 시 데이터베이스가 읽기 전용 모드로 연결**되었습니다. 이로 인해 데이터 수정 작업(`INSERT`)이 차단되면서 에러가 발생했습니다.

### 해결 방법
`saveTodo` 메서드에 `@Transactional`을 추가하여, **해당 메서드만 읽기-쓰기 모드로 설정**했습니다.

---