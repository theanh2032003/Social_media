package com.example.demo.repository;

import com.example.demo.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

    @Query("select a from GroupChat a where (a.user1.id = ?1 and a.user2.id = ?2) or (a.user1.id = ?2 and a.user2.id = ?1) ")
    Optional<GroupChat> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
