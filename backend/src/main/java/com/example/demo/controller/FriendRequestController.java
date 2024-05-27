package com.example.demo.controller;

import com.example.demo.file.FileUpLoad;
import com.example.demo.mapper.impl.UserMapper;
import com.example.demo.model.FriendRequest;
import com.example.demo.model.User;
import com.example.demo.request.ReceiveDto;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.impl.FriendRequestServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Controller
public class FriendRequestController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FriendRequestServiceImpl friendRequestService;

    @Autowired
    private FileUpLoad fileUpLoad;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/add_friend")
    @SendTo("/topic/friend_request")
    public FriendRequest addFriend(@Payload ReceiveDto receiveDto){
        System.out.println("receiveId");

        Long receiveId = receiveDto.getReceiveId();
        System.out.println(receiveId);
        Long userId = receiveDto.getUserId();
        Optional<User> user = userService.findById(userId);
        Optional<User> receiveUser = userService.findById(receiveId);
        FriendRequest friendRequest = friendRequestService.save(new FriendRequest(user.get(), receiveUser.get()));

        return friendRequest;
    }

//    @MessageMapping("/get_all_friend_request")
//    @SendTo("/topic/friend_request")
//    public List<FriendRequest> getAllFriendRequest(HttpServletRequest request){
//        Long userId = jwtProvider.getIdFromHttpRequest(request);
//        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestOfUser(userId);
//
//        return friendRequests;
//
//    }
}
