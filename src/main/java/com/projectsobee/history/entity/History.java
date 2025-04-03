package com.projectsobee.history.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class History {
    private Long id;
    private String entityType;
    private Long entityId;
    private String action;
    private String beforeData;
    private String afterData;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
} 