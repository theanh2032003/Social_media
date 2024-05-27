package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    private LocalDateTime createAt;

    @OneToMany(mappedBy = "ancestor", cascade = CascadeType.ALL)
    private List<CommentClosure> descendants;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    public Comment(String content, User user, LocalDateTime createAt, List<CommentClosure> descendants, Post post) {
        this.content = content;
        this.user = user;
        this.createAt = createAt;
        this.descendants = descendants;
        this.post = post;
    }
}
