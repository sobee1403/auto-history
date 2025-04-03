package com.projectsobee.history.service;

import com.projectsobee.history.domain.Post;
import com.projectsobee.history.mapper.PostMapper;
import com.projectsobee.history.annotation.TrackHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostMapper postMapper;

    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<Post> getAllPosts() {
        return postMapper.findAll();
    }

    public Post getPost(Long id) {
        return postMapper.findById(id);
    }

    @Transactional
    @TrackHistory(entityType = "Post", action = "CREATE")
    public Post createPost(Post post) {
        postMapper.insert(post);
        return post;
    }

    @Transactional
    @TrackHistory(entityType = "Post", action = "UPDATE")
    public Post updatePost(Post post) {
        postMapper.update(post);
        return post;
    }

    @Transactional
    @TrackHistory(entityType = "Post", action = "DELETE")
    public void deletePost(Long id) {
        postMapper.delete(id);
    }
} 