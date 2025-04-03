package com.projectsobee.history.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsobee.history.annotation.TrackHistory;
import com.projectsobee.history.entity.History;
import com.projectsobee.history.mapper.HistoryMapper;
import com.projectsobee.history.domain.Post;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class HistoryAspect {
    private final HistoryMapper historyMapper;
    private final ObjectMapper objectMapper;
    private final ThreadLocal<Map<String, Object>> beforeData = new ThreadLocal<>();

    @Before("@annotation(trackHistory)")
    public void beforeMethod(JoinPoint joinPoint, TrackHistory trackHistory) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Post) {
            Map<String, Object> data = new HashMap<>();
            data.put("data", args[0]);
            beforeData.set(data);
        }
    }

    @AfterReturning(pointcut = "@annotation(trackHistory)", returning = "result")
    public void afterMethod(JoinPoint joinPoint, TrackHistory trackHistory, Object result) {
        try {
            History history = new History();
            history.setEntityType(trackHistory.entityType());
            history.setAction(trackHistory.action());
            history.setModifiedBy("system");

            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof Post) {
                Post post = (Post) args[0];
                history.setEntityId(post.getId());
                history.setAfterData(objectMapper.writeValueAsString(post));
            }

            Map<String, Object> before = beforeData.get();
            if (before != null && before.get("data") instanceof Post) {
                history.setBeforeData(objectMapper.writeValueAsString(before.get("data")));
            }

            historyMapper.insertHistory(history);
            beforeData.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 