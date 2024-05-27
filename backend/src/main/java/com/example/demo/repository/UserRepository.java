package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

import jakarta.transaction.Transactional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByTextId(String textId);

    @Modifying
    @Transactional
    @Query("update User a set a.enable = true where a.email = ?1")
    void updateEnableUser(String email);

    @Query("select a from User a where (a.fullname like %?1% or a.textId like %?1%) and a.enable = true and a.id <> ?2")
    List<User> searchUsers(String text, Long userId);

    @Query("select a from User a where (a.fullname like %?1% or a.textId like %?1%) and a.id <> ?2 and "+
    "((a.id in (select b.friend.id from UserFriend b where b.user.id = ?2)) or "+
    "(a.id in (select b.user.id from UserFriend b where b.friend.id = ?2)))")
    List<User> searchUsersInListFriend(String text, Long userId);

    @Query("select a from User a where a.id <> ?1 and " +
            "((a.id in (select b.friend.id from UserFriend b where b.user.id = ?1)) or " +
            "(a.id in (select b.user.id from UserFriend b where b.friend.id = ?1)))")
    List<User> getAllFriend(Long userId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1 AND u.enable = true")
    boolean existsByEmailAndEnableTrue(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.textId = ?1 AND u.enable = true")
    boolean existsByTextIdAndEnableTrue(String textId);
}

