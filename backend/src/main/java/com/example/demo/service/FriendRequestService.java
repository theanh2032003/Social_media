package com.example.demo.service;

import com.example.demo.model.FriendRequest;
import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendRequestService {
    FriendRequest save(FriendRequest friendRequest);

    List<FriendRequest> getAllFriendRequestOfUser(Long userId);

    void confirmFriendRequest(Long friendRequestId);

    Optional<FriendRequest> findBySenderAndReceive(User user, User user1);

    void delete(FriendRequest friendRequest);

    Optional<FriendRequest> findById(Long friendRequestId);
}
