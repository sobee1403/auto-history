# Auto History System with Spring AOP

Spring AOP와 커스텀 어노테이션을 활용하여 엔티티의 변경 이력을 자동으로 기록하는 시스템을 구현

## 핵심 기능

- 커스텀 어노테이션을 통한 손쉬운 히스토리 추적 설정
- AOP를 활용한 자동 이력 기록
- 엔티티의 변경 전/후 데이터 저장
- 변경 유형(CREATE, UPDATE, DELETE) 구분

## AOP(Aspect-Oriented Programming)란?

AOP는 관점 지향 프로그래밍의 약자로, 핵심 비즈니스 로직과 부가 기능을 분리하여 모듈화하는 프로그래밍 패러다임

### AOP 주요 개념
- **Aspect**: 부가 기능을 모듈화한 것 (예: 이력 기록, 로깅, 트랜잭션 등)
- **Advice**: 부가 기능의 구현체, 언제 어떤 기능을 실행할지 정의
  - @Before: 메서드 실행 전
  - @After: 메서드 실행 후 (정상 종료와 예외 발생 모두)
  - @AfterReturning: 메서드가 정상적으로 실행된 후
  - @AfterThrowing: 메서드에서 예외가 발생한 후
  - @Around: 메서드 실행 전후
- **Pointcut**: 부가 기능을 적용할 지점 지정
- **JoinPoint**: 부가 기능이 적용될 수 있는 모든 지점

### 메서드 실행 순서
```
클라이언트 호출
  │
  ▼
@Before 실행 (beforeMethod)
  │
  ▼
실제 비즈니스 메서드 실행
  │
  ▼
@AfterReturning 실행 (afterMethod)
  │
  ▼
클라이언트에 결과 반환
```

## 커스텀 어노테이션

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackHistory {
    String entityType();
    String action();
}
```

- `@Target(ElementType.METHOD)`: 메서드 레벨에만 어노테이션 적용 가능
- `@Retention(RetentionPolicy.RUNTIME)`: 런타임에도 어노테이션 정보 유지
- `entityType`: 변경되는 엔티티 타입 지정
- `action`: 수행되는 작업 유형 지정 (CREATE, UPDATE, DELETE)

## 동작 방식

### 1. 히스토리 추적 설정
```java
@TrackHistory(entityType = "Post", action = "CREATE")
public Post createPost(Post post) {
    postMapper.insert(post);
    return post;
}
```

### 2. AOP를 통한 자동 기록
```java
@Aspect
@Component
public class HistoryAspect {
    @Before("@annotation(trackHistory)")
    public void beforeMethod(JoinPoint joinPoint, TrackHistory trackHistory) {
        // 변경 전 데이터 저장
    }

    @AfterReturning(pointcut = "@annotation(trackHistory)", returning = "result")
    public void afterMethod(JoinPoint joinPoint, TrackHistory trackHistory, Object result) {
        // 변경 후 데이터 저장 및 히스토리 기록
    }
}
```

## 메서드별 기능 설명

### 1. beforeMethod
- 메서드 실행 전 호출
- 변경 전 데이터를 ThreadLocal에 저장
- 동시성 문제 방지를 위해 ThreadLocal 사용

### 2. afterMethod
- 메서드 실행 후 호출
- 변경된 엔티티 정보 수집
- 히스토리 테이블에 기록
  - entityType: 엔티티 종류
  - entityId: 엔티티 식별자
  - action: 수행된 작업
  - beforeData: 변경 전 데이터 (JSON)
  - afterData: 변경 후 데이터 (JSON)
  - modifiedBy: 수정자
  - modifiedAt: 수정 시간

## 데이터베이스 스키마

```sql
CREATE TABLE history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL,
    before_data TEXT,
    after_data TEXT,
    modified_by VARCHAR(50) NOT NULL,
    modified_at DATETIME NOT NULL
);
```

## 사용 예시

1. 엔티티 생성 시
```json
{
    "entityType": "Post",
    "entityId": 1,
    "action": "CREATE",
    "beforeData": null,
    "afterData": {"id":1,"title":"테스트 제목","content":"테스트 내용"},
    "modifiedBy": "system"
}
```

2. 엔티티 수정 시
```json
{
    "entityType": "Post",
    "entityId": 1,
    "action": "UPDATE",
    "beforeData": {"id":1,"title":"이전 제목","content":"이전 내용"},
    "afterData": {"id":1,"title":"수정된 제목","content":"수정된 내용"},
    "modifiedBy": "system"
}
```

## 장점

1. **관심사의 분리**
   - 비즈니스 로직과 히스토리 기록 로직을 분리
   - 코드의 가독성과 유지보수성 향상

2. **재사용성**
   - 어노테이션만 추가하면 어떤 엔티티든 히스토리 추적 가능
   - 중복 코드 제거

3. **확장성**
   - 새로운 엔티티 추가 시 어노테이션만 추가하면 됨
   - 히스토리 기록 방식 변경 시 AOP 코드만 수정

## 주의사항

1. ThreadLocal 사용 시 메모리 누수 방지를 위해 반드시 remove() 호출
2. 트랜잭션 고려 필요 (히스토리 저장이 실패하면?)
3. JSON 데이터 크기가 커질 경우 데이터베이스 성능 고려 


참고 : https://docs.spring.io/spring-framework/reference/core/aop.html