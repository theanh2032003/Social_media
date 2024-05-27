package com.example.demo.service.impl;

import com.example.demo.model.PostAnalytic;
import com.example.demo.repository.PostAnalyticRepository;
import com.example.demo.service.PostAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostAnalyticServiceImpl implements PostAnalyticService {

    @Autowired
    private PostAnalyticRepository postAnalyticRepo;

    @Override
    public Long getLikesOfPost(Long postId) {
        Optional<PostAnalytic> postAnalytic = postAnalyticRepo.findByPostId(postId);
        return postAnalytic.get().getLikeCount();
    }
}
