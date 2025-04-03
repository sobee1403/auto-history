package com.projectsobee.history.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsobee.history.annotation.TrackHistory;
import com.projectsobee.history.entity.History;
import com.projectsobee.history.mapper.HistoryMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 엔티티 변경 이력을 자동으로 기록하는 AOP Aspect
 * 
 * @TrackHistory 어노테이션이 붙은 메서드의 실행 전후에 데이터 변경 사항을 감지하여
 * history 테이블에 자동으로 기록
 * 
 * 동작 방식:
 * 1. @Before: 메서드 실행 전에 변경 전 데이터를 ThreadLocal에 저장
 * 2. @AfterReturning: 메서드 실행 후에 변경 후 데이터와 함께 히스토리 저장
 */
@Aspect
@Component
@RequiredArgsConstructor
public class HistoryAspect {
    private final HistoryMapper historyMapper;
    private final ObjectMapper objectMapper;
    // ThreadLocal을 사용하여 메서드 실행 전후의 데이터를 스레드 안전하게 관리
    private final ThreadLocal<Map<String, Object>> beforeData = new ThreadLocal<>();

    /**
     * 메서드 실행 전에 호출되어 변경 전 데이터를 저장
     * 
     * @param joinPoint AOP 조인 포인트
     * @param trackHistory 적용된 @TrackHistory 어노테이션
     */
    @Before("@annotation(trackHistory)")
    public void beforeMethod(JoinPoint joinPoint, TrackHistory trackHistory) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("data", args[0]);
            beforeData.set(data);
        }
    }

    /**
     * 메서드 실행 후에 호출되어 변경 이력을 저장
     * 
     * @param joinPoint AOP 조인 포인트
     * @param trackHistory 적용된 @TrackHistory 어노테이션
     * @param result 메서드 실행 결과
     */
    @AfterReturning(pointcut = "@annotation(trackHistory)", returning = "result")
    public void afterMethod(JoinPoint joinPoint, TrackHistory trackHistory, Object result) {
        try {
            History history = new History();
            history.setEntityType(trackHistory.entityType());
            history.setAction(trackHistory.action());
            history.setModifiedBy("system");

            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                Object entity = args[0];
                // Reflection을 사용하여 getId 메서드 호출
                try {
                    Long entityId = (Long) entity.getClass().getMethod("getId").invoke(entity);
                    history.setEntityId(entityId);
                } catch (Exception e) {
                    // getId 메서드가 없는 경우 처리
                    history.setEntityId(null);
                }
                history.setAfterData(objectMapper.writeValueAsString(entity));
            }

            Map<String, Object> before = beforeData.get();
            if (before != null) {
                history.setBeforeData(objectMapper.writeValueAsString(before.get("data")));
            }

            historyMapper.insertHistory(history);
            // 메모리 누수 방지를 위해 ThreadLocal 데이터 제거
            beforeData.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 