package com.example.demo.mapper.impl;

import com.example.demo.mapper.ObjectMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentClosure;
import com.example.demo.model.dto.CommentDto;
import com.example.demo.repository.CommentClosureRepository;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentMapper implements ObjectMapper<Comment, CommentDto> {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentClosureRepository commentClosureRepo;

    @Autowired
    private UserMapper userMapper;

    @Override
    public CommentDto mapTo(Comment comment) {

        List<Long> descendantIds = commentClosureRepo.getDescendantHaveDepthEqual1(comment.getId());

        return new CommentDto(comment.getId(),comment.getContent(),userMapper.mapTo(comment.getUser()),
                comment.getCreateAt(),descendantIds,comment.getPost().getId());
    }

    @Override
    public Comment mapFrom(CommentDto commentDto) {
        Optional<Comment> comment = commentRepository.findById(commentDto.getId());
        if(comment.isEmpty()){
            return null;
        }
        return comment.get();
    }
}
