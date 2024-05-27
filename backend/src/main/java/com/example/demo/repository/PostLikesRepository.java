package com.example.demo.repository;

import com.example.demo.model.PostLikes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes,Long> {



    @Modifying
    @Transactional
    @Query("delete from PostLikes a where a.user.id = ?1 and a.post.id = ?2")
    void deleteAllByUserIdAndPostId(Long userId,Long postId);

    void deleteAllByPostId(Long postId);

    Optional<PostLikes> findByUserIdAndPostId(Long userId,Long postId);
}
