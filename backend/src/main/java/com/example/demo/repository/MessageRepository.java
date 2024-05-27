package com.example.demo.repository;

import com.example.demo.model.Comment;
import com.example.demo.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByGroupChatId(Long groupchatId);

    @Modifying
    @Transactional
    @Query("update Message a set a.isDeleted = true where a.id = ?1")
    Message deleteMessage(Long messageId);
}
