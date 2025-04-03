package com.projectsobee.history.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 엔티티 변경 이력을 저장하는 History 엔티티
 * 
 * 엔티티의 변경 사항이 발생할 때마다 해당 내역을 저장합니다.
 * beforeData와 afterData는 JSON 형식으로 저장됩니다.
 */
@Getter
@Setter
public class History {
    /**
     * 히스토리 레코드의 고유 식별자
     */
    private Long id;

    /**
     * 변경된 엔티티의 타입 (예: "Post", "User" 등)
     */
    private String entityType;

    /**
     * 변경된 엔티티의 ID
     */
    private Long entityId;

    /**
     * 수행된 작업 (CREATE, UPDATE, DELETE)
     */
    private String action;

    /**
     * 변경 전 데이터 (JSON 형식)
     */
    private String beforeData;

    /**
     * 변경 후 데이터 (JSON 형식)
     */
    private String afterData;

    /**
     * 변경을 수행한 사용자
     */
    private String modifiedBy;

    /**
     * 변경이 발생한 시간
     */
    private LocalDateTime modifiedAt;
} 