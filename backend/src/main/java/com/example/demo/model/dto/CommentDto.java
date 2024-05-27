package com.example.demo.model.dto;

import com.example.demo.mapper.impl.UserMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentClosure;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDto {

    private Long id;

    private String content;

    private UserDto userDto;

    private LocalDateTime createAt;

    private List<Long> descendants;

    private Long postId;

    public CommentDto(Long id, String content, UserDto userDto, LocalDateTime createAt, List<Long> descendants, Long postId) {
        this.id = id;
        this.content = content;
        this.userDto = userDto;
        this.createAt = createAt;
        this.descendants = descendants;
        this.postId = postId;
    }

}
