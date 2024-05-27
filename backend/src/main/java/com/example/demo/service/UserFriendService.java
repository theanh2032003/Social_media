package com.example.demo.service;

import com.example.demo.model.UserFriend;

import java.util.Optional;

public interface UserFriendService {
    void deleteUserFriend(Long userId, Long friendId);

    Optional<UserFriend> findByUserFriendByUserIdAndFriendId(Long userId, Long friendId);
}
