package com.example.demo.service.impl;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentClosure;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentClosureRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreateCommentRequest;
import com.example.demo.request.PatchCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepo;



    @Autowired
    private PostRepository postRepo;

    @Autowired
    private CommentClosureRepository commentClosureRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Comment save(CreateCommentRequest commentRequest, Long userId) {
        System.out.println(-1);
        Optional<Post> post = postRepo.findById(commentRequest.getPostId());
        System.out.println('0');
        if(post.isEmpty()){
            throw new IllegalArgumentException("post not found");
        }

        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){
            throw new IllegalArgumentException("user not found");
        }


        Comment comment = new Comment().builder()
                .content(commentRequest.getContent())
                .user(user.get())
                .post(post.get())
                .createAt(LocalDateTime.now())
                .descendants(null)
                .build();

        commentRepo.save(comment);
        System.out.println('1');
        Comment descendantComment = commentRepo.findById(comment.getId()).get();
        Long ancestorId = (commentRequest.getAncestor() != null)? commentRequest.getAncestor() : descendantComment.getId();
        System.out.println('2');

        Optional<Comment> ancestor = commentRepo.findById(ancestorId);
        if(ancestor.isEmpty()){
            throw new IllegalArgumentException("not found ancestor comment");
        }
        if(commentRequest.getAncestor() == null){
            commentClosureRepo.save(new CommentClosure(descendantComment,descendantComment,0));
        }else{
            commentClosureRepo.save(new CommentClosure(descendantComment,descendantComment,0));
            commentClosureRepo.insertInCommentClosuresecond(ancestor.get(), descendantComment);
        }



        return comment;
    }

    @Override
    public List<Comment> getAllCommentByPostId(Long postId) {
        return commentRepo.findAllByPostId(postId);
    }

    @Override
    public void deleteCommentById(Long commentId) {
        // TODO Auto-generated method stub
        commentRepo.deleteDescandantCommentByAncestorId(commentId);
        commentRepo.deleteById(commentId);
        commentClosureRepo.deleteAllByAncestorId(commentId);

    }

    @Override
    public void deleteAllCommentByPostId(Long postId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllCommentByUserId(Long userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Comment changeContentCommnent(Long commentId, String content) {
        Optional<Comment> existComment = commentRepo.findById(commentId);
        if(existComment.isEmpty()){
            throw new IllegalArgumentException("comment not found");
        }
        existComment.get().setContent(content);
        return commentRepo.save(existComment.get());


    }

    @Override
    public void changeContentComment(PatchCommentRequest commentRequest) {
        Optional<Comment> existComment = commentRepo.findById(commentRequest.getId());
        if(existComment.isEmpty()){
            throw new IllegalArgumentException("comment not found");
        }
        existComment.get().setContent(commentRequest.getContent());
    }

    @Override
    public Long getNumOfDescendant(Long cmtId) {
        return commentClosureRepo.getNumOfDescendant(cmtId);
    }

    @Override
    public Optional<Comment> findById(Long cmtId) {
        return commentRepo.findById(cmtId);
    }

    @Override
    public List<Comment> getAllFartherCmtByPostId(Long postId) {
        return commentRepo.getAllFartherCmt(postId);
    }

}
