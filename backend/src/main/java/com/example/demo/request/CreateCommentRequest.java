package com.example.demo.request;

import com.example.demo.model.CommentClosure;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateCommentRequest {

    private String content;


    private Long postId;

    private Long ancestor;
}
