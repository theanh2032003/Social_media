package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    @Query("select a from Post a where a.user.id = ?1 or a.user.id in (select b.friend.id from UserFriend b where b.user.id = ?1 ) "+
            "or a.user.id in (select b.user.id from UserFriend b where b.friend.id = ?1 )")
    List<Post> getAllPostOfFriend(Long userId);

    List<Post> findByUserId(Long userId);

    @Query("select a from Post a where a.user.id <> ?1 and a.id in (select b.post.id from PostLikes b where b.user.id = ?1)")
    List<Post> findWhichUserLike(Long userId);


}
