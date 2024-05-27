package com.example.demo.service.impl;

import com.example.demo.model.UserFriend;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    private UserFriendRepository userFriendRepo;

    @Override
    public void deleteUserFriend(Long userId, Long friendId) {
        userFriendRepo.deleteUserFriendByUserAndFriend(userId,friendId);
    }

    @Override
    public Optional<UserFriend> findByUserFriendByUserIdAndFriendId(Long userId, Long friendId) {
        return userFriendRepo.findByUserIdAndFriendId(userId, friendId);
    }
}
