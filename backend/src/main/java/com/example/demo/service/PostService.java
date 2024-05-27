package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Post;

public interface PostService {

    Post saveNewPost(Post post);

    Post updatePost(Post post);
    void deletePostById(Long id);

    List<Post> getAllPostByFriend(Long userId);

    List<Post> getAllPostByUserId(Long userId);

    List<Post> getAllPostByUserLike(Long userId);

    Optional<Post> findById(Long postId);

    void likePost(Long userId, Long postId);

    void unlikePost(Long userId, Long postId);

    boolean checkIfUserIsLikePost(Long userId, Long postId);
}
