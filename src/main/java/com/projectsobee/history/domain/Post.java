package com.projectsobee.history.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 게시글 엔티티
 * 
 * 게시글 정보를 저장하는 엔티티
 * 
 * @Data 어노테이션을 사용하여 모든 필드에 대한 getter, setter 메서드를 자동으로 생성
 */
@Data
public class Post {
    private Long id;                    // 게시글 고유 ID
    private String title;               // 게시글 제목
    private String content;             // 게시글 내용
    private String author;              // 게시글 작성자
    private LocalDateTime createdAt;    // 게시글 생성 시간
    private LocalDateTime updatedAt;    // 게시글 수정 시간

}