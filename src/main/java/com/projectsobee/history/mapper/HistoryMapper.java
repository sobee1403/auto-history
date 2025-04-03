package com.projectsobee.history.mapper;

import com.projectsobee.history.entity.History;
import org.apache.ibatis.annotations.Mapper;

/**
 * 히스토리 데이터의 데이터베이스 접근을 담당하는 MyBatis Mapper
 * 
 * history 테이블에 대한 CRUD 작업을 수행합니다.
 * 실제 SQL 쿼리는 mapper/HistoryMapper.xml 파일에 정의되어 있습니다.
 */
@Mapper
public interface HistoryMapper {
    /**
     * 새로운 히스토리 레코드를 저장
     * 
     * @param history 저장할 히스토리 데이터
     */
    void insertHistory(History history);
} 