package com.example.demo.controller;

import com.example.demo.mapper.ObjectMapper;
import com.example.demo.mapper.impl.CommentMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.dto.CommentDto;
import com.example.demo.request.CreateCommentRequest;
import com.example.demo.request.ListLongRequest;
import com.example.demo.request.PatchCommentRequest;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.impl.CommentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cmt")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/{postId}")
    public ResponseEntity<?> getCommentInPost(@PathVariable  Long postId){
        List<Comment> comments = commentService.getAllCommentByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(i -> commentMapper.mapTo(i)).collect(Collectors.toList());
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest commentRequest, HttpServletRequest request){
        String jwtToken = jwtProvider.parseJwtToken(request);
        Long userId = jwtProvider.getIdByJwtToken(jwtToken);
        Comment createComment = commentService.save(commentRequest, userId);
        return new ResponseEntity<>(commentMapper.mapTo(createComment) ,HttpStatus.CREATED);
    }

    @PatchMapping("/change")
    public ResponseEntity<?> changeComment(@RequestBody PatchCommentRequest commentRequest){
        commentService.changeContentComment(commentRequest);
        return new ResponseEntity<>("comment has been changed",HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);
        return new ResponseEntity<>("Comment has been deleted",HttpStatus.OK);
    }

    @GetMapping("/descendant/{cmtId}")
    public ResponseEntity<?> getNumDescendantOfCmt(@PathVariable Long cmtId){
        Long numOfDescendants = commentService.getNumOfDescendant(cmtId);
        return new ResponseEntity<>(numOfDescendants,HttpStatus.OK);
    }

    @PostMapping("/get_descendant")
    public ResponseEntity<?> getDescendantCmt(@RequestBody ListLongRequest request){
        List<CommentDto> commentDtos = new ArrayList<>();
        List<Long> descendantIds = request.getList();
        descendantIds.forEach((descendantId)-> {
            Comment comment = commentService.findById(descendantId).get();
            commentDtos.add(commentMapper.mapTo(comment));
        });
        return new ResponseEntity<>(commentDtos,HttpStatus.OK);
    }

    @GetMapping("/get_ancestor/{postId}")
    public ResponseEntity<?> getAncestorOfPost(@PathVariable Long postId){
        List<Comment> comments = commentService.getAllFartherCmtByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(i -> commentMapper.mapTo(i)).collect(Collectors.toList());
        return new ResponseEntity<>(commentDtos,HttpStatus.OK);
    }
}
