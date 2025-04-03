package com.projectsobee.history.mapper;

import com.projectsobee.history.entity.History;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HistoryMapper {
    void insertHistory(History history);
} 