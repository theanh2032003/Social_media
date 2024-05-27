package com.example.demo.repository;

import com.example.demo.model.PostAnalytic;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostAnalyticRepository extends JpaRepository<PostAnalytic,Long> {
    @Modifying
    @Transactional
    @Query("update PostAnalytic a set a.likeCount = a.likeCount + 1 where a.id = ?1")
    void addLikeOfPost(Long postId);

    @Modifying
    @Transactional
    @Query("update PostAnalytic a set a.likeCount = a.likeCount - 1 where a.id = ?1")
    void minusLikeOfPost(Long postId);

    @Modifying
    @Transactional
    @Query("delete PostAnalytic a where a.id = ?1")
    void deletePostAnalyticByPostId(Long postId);

    Optional<PostAnalytic> findByPostId(Long postId);
}
