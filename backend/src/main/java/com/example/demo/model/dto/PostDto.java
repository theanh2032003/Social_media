package com.example.demo.model.dto;

import com.example.demo.mapper.impl.CommentMapper;
import com.example.demo.mapper.impl.UserMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.PostAnalytic;
import com.example.demo.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class PostDto {

    private Long id;

    private String content;

    private LocalDateTime createAt;

    private UserDto user;

    private List<String> images = new ArrayList<>();

    public PostDto(Long id, String content, LocalDateTime createAt, UserDto user, List<String> images) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.user = user;
        this.images = images;
    }
}
