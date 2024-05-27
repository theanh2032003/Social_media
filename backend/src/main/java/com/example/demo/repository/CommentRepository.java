package com.example.demo.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Modifying
    @Transactional
    @Query("delete from CommentClosure a where a.ancestor = ?1 or a.descendant = ?1")
    void deleteInClosureById(Long id);

    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Comment a where a.post.id = ?1")
    void deleteAllByPostId(Long postId);

    void deleteAllByUserId(Long userId);

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findByUserIdAndPostId(Long userId, Long postId, Sort sort);

    @Query("select a from Comment a "+
            "join CommentClosure b on (a.id = b.descendant.id) "+
            "where b.ancestor.id = ?1")
    List<Comment> findDescendantsOfComment(Long commentId);

    @Query("select a from Comment a " +
            "join CommentClosure b on (a.id = b.ancestor.id) "+
            "where b.descendant.id = ?1")
    List<Comment> findAncestorOfComment(Long commentId);



    @Modifying
    @Transactional
    @Query("delete from Comment a where a.id in "+
            "(select b.descendant.id from CommentClosure b where b.ancestor.id = ?1)")
    void deleteDescandantCommentByAncestorId(Long ancestorId);

    @Query("select a from Comment a where a.post.id = ?1 and a.id not in " +
            "(select b.descendant.id from CommentClosure b where b.ancestor.id <> b.descendant.id )")
    List<Comment> getAllFartherCmt(Long postId);

}
