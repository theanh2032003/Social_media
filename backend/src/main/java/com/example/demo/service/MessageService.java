package com.example.demo.service;

import com.example.demo.model.GroupChat;
import com.example.demo.model.Message;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> getAllMessageOfGroupChat(Long groupchatId);

    Message sendMessage(Long receiverId, Long senderId, String content );

    Message changeMessage(Long messageId, String content);

    Optional<GroupChat> findGroupChatByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    List<Message> getAllMessageByGroupChatId(Long groupChatId);

    Message save(Message message);

    Message deleteMessage(Long messageId);
}
