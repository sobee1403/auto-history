package com.projectsobee.history.mapper;

import com.projectsobee.history.domain.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM post")
    List<Post> findAll();

    @Select("SELECT * FROM post WHERE id = #{id}")
    Post findById(Long id);

    @Insert("INSERT INTO post (title, content, author, created_at, updated_at) " +
            "VALUES (#{title}, #{content}, #{author}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Post post);

    @Update("UPDATE post SET title = #{title}, content = #{content}, " +
            "updated_at = NOW() WHERE id = #{id}")
    void update(Post post);

    @Delete("DELETE FROM post WHERE id = #{id}")
    void delete(Long id);
} 