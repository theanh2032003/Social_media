package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.request.CreateCommentRequest;
import com.example.demo.request.PatchCommentRequest;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment save(CreateCommentRequest commentRequest, Long userId);

    List<Comment> getAllCommentByPostId(Long postId);

    void deleteCommentById(Long id);

    void deleteAllCommentByPostId(Long postId);

    void deleteAllCommentByUserId(Long userId);

    Comment changeContentCommnent(Long commentId, String content);


    void changeContentComment(PatchCommentRequest commentRequest);

    Long getNumOfDescendant(Long cmtId);

    Optional<Comment> findById(Long descendantId);

    List<Comment> getAllFartherCmtByPostId(Long postId);
}