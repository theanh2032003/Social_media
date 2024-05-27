package com.example.demo.mapper.impl;

import com.example.demo.model.Post;
import com.example.demo.model.PostAnalytic;
import com.example.demo.model.dto.PostDto;
import com.example.demo.repository.PostAnalyticRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostMapper {

    @Autowired
    private PostAnalyticRepository postAnalyticRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserMapper userMapper;

    public PostDto mapTo(Post post){

        return new PostDto(post.getId(),post.getContent(),post.getCreateAt(),userMapper.mapTo(post.getUser()),post.getImages());
    }

    public Post mapFrom(PostDto postDto){
        return postRepo.findById(postDto.getId()).get();
    }
}
