package com.example.demo.repository;

import com.example.demo.model.UserFriend;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    @Modifying
    @Transactional
    @Query("delete from UserFriend a where (a.user.id = ?1 and a.friend.id = ?2) or (a.user.id = ?2 and a.friend.id = ?1)")
    void deleteUserFriendByUserAndFriend(Long userId, Long friendId);

    @Query("select a from UserFriend a where (a.user.id = ?1 and a.friend.id = ?2) or (a.user.id = ?2 and a.friend.id = ?1)")
    Optional<UserFriend> findByUserIdAndFriendId(Long userId, Long friendId);
}
