package com.example.demo.service.impl;

import com.example.demo.model.FriendRequest;
import com.example.demo.model.User;
import com.example.demo.model.UserFriend;
import com.example.demo.repository.FriendRequestRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepo;

    @Autowired
    private UserFriendRepository userFriendRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        return friendRequestRepo.save(friendRequest);
    }

    @Override
    public List<FriendRequest> getAllFriendRequestOfUser(Long userId) {
        return friendRequestRepo.getAllFriendRequestByReceive(userId);
    }

    @Override
    public void confirmFriendRequest(Long friendRequestId) {
        friendRequestRepo.confirmFriendRequest(friendRequestId);
        Optional<FriendRequest> friendRequest = friendRequestRepo.findById(friendRequestId);
        Optional<User> sender = userRepo.findById(friendRequest.get().getSender().getId());
        Optional<User> receive = userRepo.findById(friendRequest.get().getReceiver().getId());

        userFriendRepo.save(new UserFriend(sender.get(), receive.get()));

    }

    @Override
    public Optional<FriendRequest> findBySenderAndReceive(User sender, User receiver) {
        return friendRequestRepo.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
    }

    @Override
    public void delete(FriendRequest friendRequest) {
        friendRequestRepo.delete(friendRequest);
    }

    @Override
    public Optional<FriendRequest> findById(Long friendRequestId) {
        return friendRequestRepo.findById(friendRequestId);
    }
}
