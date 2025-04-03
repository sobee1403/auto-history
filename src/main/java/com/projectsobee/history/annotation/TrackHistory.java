package com.projectsobee.history.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 엔티티의 변경 이력을 자동으로 추적하기 위한 커스텀 어노테이션
 * 
 * Service 계층의 메서드에 이 어노테이션을 붙이면 해당 메서드 실행 전후의
 * 데이터 변경 사항이 자동으로 history 테이블에 기록됩니다.
 * 
 * 사용 예시:
 * @TrackHistory(entityType = "Post", action = "CREATE")
 * public Post createPost(Post post) { ... }
 * 
 * @see com.projectsobee.history.aspect.HistoryAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackHistory {
    /**
     * 변경되는 엔티티의 타입
     * 예: "Post", "User", "Comment" 등
     */
    String entityType();

    /**
     * 수행되는 작업의 종류
     * 예: "CREATE", "UPDATE", "DELETE" 등
     */
    String action();
} 