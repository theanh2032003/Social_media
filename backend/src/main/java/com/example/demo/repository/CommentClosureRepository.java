package com.example.demo.repository;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentClosure;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentClosureRepository extends JpaRepository<CommentClosure,Long> {

    void deleteAllByAncestorId(Long ancestorId);

    @Query("select count(a) from CommentClosure a where a.ancestor.id = ?1 and a.depth = 1")
    Long getNumOfDescendant(Long cmtId);

    @Modifying
    @Transactional
    @Query("delete from CommentClosure a where a.descendant.id in "+
            "(select b.id from Comment b where b.post.id =?1 )")
    void deleteAllCommentByPostId(Long postId);



    @Modifying
    @Transactional
    @Query("INSERT INTO CommentClosure (ancestor, descendant, depth) " +
            "SELECT cc.ancestor, ?2, cc.depth + 1 " +
            "FROM CommentClosure cc " +
            "WHERE cc.descendant = ?1")
    void insertInCommentClosuresecond(Comment ancestor, Comment descendant);

    @Query("select a.descendant.id from CommentClosure a where a.ancestor.id = ?1 and a.depth = 1")
    List<Long> getDescendantHaveDepthEqual1(Long id);
}
