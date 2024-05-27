package com.example.demo.service.impl;

import com.example.demo.model.GroupChat;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.GroupChatRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private GroupChatRepository groupChatRepo;

    @Override
    public List<Message> getAllMessageOfGroupChat(Long groupchatId) {
        return messageRepo.findAllByGroupChatId(groupchatId);
    }

    @Override
    public Message sendMessage(Long receiverId, Long senderId, String content) {

        Optional<User> sender = userRepo.findById(senderId);
        Optional<User> receiver = userRepo.findById(receiverId);


        boolean isExist = groupChatRepo.findByUser1IdAndUser2Id(receiverId,senderId).isPresent();
        if(!isExist){
            groupChatRepo.save(new GroupChat(sender.get(),receiver.get()));
        }

        Optional<GroupChat> groupChat = groupChatRepo.findByUser1IdAndUser2Id(senderId,receiverId);
        Message message = messageRepo.save(new Message(content, LocalDateTime.now(),sender.get(),groupChat.get()));



        return message;
    }

    @Override
    public Message changeMessage(Long messageId, String content) {

        Optional<Message> message = messageRepo.findById(messageId);
        message.get().setText(content);

        return messageRepo.save(message.get());
    }


    @Override
    public List<Message> getAllMessageByGroupChatId(Long groupChatId) {
        return messageRepo.findAllByGroupChatId(groupChatId);
    }

    @Override
    public Message save(Message message) {
        return messageRepo.save(message);
    }

    @Override
    public Message deleteMessage(Long messageId) {
        return messageRepo.deleteMessage(messageId);
    }

    @Override
    public Optional<GroupChat> findGroupChatByUser1IdAndUser2Id(Long user1Id, Long user2Id) {
        return groupChatRepo.findByUser1IdAndUser2Id(user1Id,user2Id);
    }
}
