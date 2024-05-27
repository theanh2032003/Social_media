package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.PostAnalytic;
import com.example.demo.model.PostLikes;
import com.example.demo.model.User;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private CommentClosureRepository commentClosureRepo;

    @Autowired
    private PostLikesRepository postLikesRepo;

    @Autowired
    private PostAnalyticRepository postAnalyticRepo;


    @Override
    public Post saveNewPost(Post post) {
        postAnalyticRepo.save(new PostAnalytic((long) 0,post));
        return postRepo.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepo.save(post);
    }


    @Override
    public void deletePostById(Long postId) {
        commentClosureRepo.deleteAllCommentByPostId(postId);
        System.out.println(1);
        commentRepo.deleteAllByPostId(postId);
        postAnalyticRepo.deletePostAnalyticByPostId(postId);
        System.out.println(2);

        postLikesRepo.deleteAllByPostId(postId);
        postRepo.deleteById(postId);

    }

    @Override
    public List<Post> getAllPostByFriend(Long userId) {
        List<Post> posts = postRepo.getAllPostOfFriend(userId);
        Collections.sort(posts, new Comparator<Post>() {

            @Override
            public int compare(Post o1, Post o2) {
                // TODO Auto-generated method stub
                return o1.getCreateAt().compareTo(o2.getCreateAt());
            }
        });
        return posts;
    }

    @Override
    public List<Post> getAllPostByUserId(Long userId) {

        return postRepo.findByUserId(userId);
    }

    @Override
    public List<Post> getAllPostByUserLike(Long userId) {
        return postRepo.findWhichUserLike(userId);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepo.findById(postId);
    }

    @Override
    public void likePost(Long userId, Long postId) {

        User user = userRepo.findById(userId).get();
        Post post = postRepo.findById(postId).get();

        postLikesRepo.save( new PostLikes( post, user, LocalDateTime.now()));
        postAnalyticRepo.addLikeOfPost(postId);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        postAnalyticRepo.minusLikeOfPost(postId);
        postLikesRepo.deleteAllByUserIdAndPostId(userId,postId);


    }

    @Override
    public boolean checkIfUserIsLikePost(Long userId, Long postId) {
        Optional<PostLikes> postLikes = postLikesRepo.findByUserIdAndPostId(userId,postId);
        if(postLikes.isEmpty()){
            return false;
        }
        return true;
    }


}

