package com.example.demo.controller;

import com.example.demo.file.FileUpLoad;
import com.example.demo.mapper.impl.UserMapper;
import com.example.demo.model.FriendRequest;
import com.example.demo.model.User;
import com.example.demo.model.UserFriend;
import com.example.demo.model.dto.UserDto;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.impl.FriendRequestServiceImpl;
import com.example.demo.service.impl.UserFriendServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

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
    private UserFriendServiceImpl userFriendService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final String PATH_IMAGE = "D:/Tai_lieu_PTIT/ttcs/Frontend/mxh/public/user/";

    private int firstIndex = 0;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId){
        Optional<User> user = userService.findById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDto userDto = userMapper.mapTo(user.get());
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PatchMapping(value = "/change_info" , produces= MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> setInfo(@RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,@RequestParam(name = "bannerFile", required = false) MultipartFile bannerFile,
                                     @RequestParam("fullname") String fullname, @RequestParam("location") String location, @RequestParam("bio") String bio,
                                     HttpServletRequest request) throws IOException {
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        Optional<User> user = userService.findById(userId);

        if(user.isEmpty()){
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
        System.out.println(avatarFile);
        String avatar = fileUpLoad.saveFile(PATH_IMAGE ,userId.toString(), new MultipartFile[]{avatarFile} ).get(firstIndex);
        String banner = fileUpLoad.saveFile(PATH_IMAGE ,userId.toString(), new MultipartFile[]{bannerFile} ).get(firstIndex);

        if(avatar != null){
            avatar = avatar.substring(avatar.indexOf("\\user"));
        }
        if(banner != null){
            banner = banner.substring(banner.indexOf("\\user"));
        }


        userService.changeInfo(user.get(), avatar, banner, fullname, bio, location);
        UserDto userDto = userMapper.mapTo(user.get());
        return new ResponseEntity<>(userDto ,HttpStatus.OK);
    }

    @GetMapping("/get_all_friend_request")
    public ResponseEntity<?> getAllFriendRequest(HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestOfUser(userId);

        return new ResponseEntity<>(friendRequests,HttpStatus.OK);

    }

    @PostMapping("/add_friend/{receiveId}")
    public ResponseEntity<?> addFriend(@PathVariable Long receiveId, HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        Optional<User> user = userService.findById(userId);
        Optional<User> receiveUser = userService.findById(receiveId);
        FriendRequest friendRequest = friendRequestService.save(new FriendRequest(user.get(), receiveUser.get()));
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestOfUser(userId);

        messagingTemplate.convertAndSend("/topic/friend-request/"+receiveId.toString(),"add friend");
        return new ResponseEntity<>("send friend request",HttpStatus.CREATED);
    }

    @DeleteMapping("/delete_friend_request")
    public ResponseEntity<?> deleteFriendRequest(@RequestParam("friendRequestId") Long friendRequestId){

        Optional<FriendRequest> friendRequest =  friendRequestService.findById(friendRequestId);
        messagingTemplate.convertAndSend("/topic/friend-request/"+friendRequest.get().getReceiver().getId().toString(),"add friend");

        friendRequestService.delete(friendRequest.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/confirm_add_friend/{friendRequestId}")
    public ResponseEntity<?> confirmAddFriend(@PathVariable Long friendRequestId){
        Optional<FriendRequest> friendRequest = friendRequestService.findById(friendRequestId);
        friendRequestService.confirmFriendRequest(friendRequestId);
        messagingTemplate.convertAndSend("/topic/friend-request/"+friendRequest.get().getReceiver().getId().toString(),"add friend");

        return new ResponseEntity<>("already friend",HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam("name") String nameUser,HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        List<UserDto> userDtos = userService.searchUser(nameUser,userId);
        return new ResponseEntity<>(userDtos,HttpStatus.OK);

    }

    @DeleteMapping("/delete_friend")
    public ResponseEntity<?> deleteFriend(@RequestParam Long friendId, HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        Optional<User> user = userService.findById(userId);
        Optional<User> friend = userService.findById(friendId);

        Optional<FriendRequest> friendRequest = friendRequestService.findBySenderAndReceive(user.get(),friend.get());
        friendRequestService.delete(friendRequest.get());
        userFriendService.deleteUserFriend(userId,friendId);
        messagingTemplate.convertAndSend("/topic/friend-request/"+friendId.toString(),"add friend");
        return new ResponseEntity<>("delete friend relationship",HttpStatus.OK);
    }

    @GetMapping("/check_add_friend")
    public ResponseEntity<?> checkStatusAddFriendRequest(@RequestParam("receiveId") Long receiveId, HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        Optional<User> user = userService.findById(userId);
        Optional<User> receiveUser = userService.findById(receiveId);

        Optional<FriendRequest> friendRequest =  friendRequestService.findBySenderAndReceive(user.get(), receiveUser.get());
        Optional<UserFriend> userFriend = userFriendService.findByUserFriendByUserIdAndFriendId(userId, receiveId);
        if(friendRequest.isEmpty() && userFriend.isEmpty()){
            return new ResponseEntity<>("Add",HttpStatus.OK);
        }

        if( userFriend.isPresent() ){
            return new ResponseEntity<>("Friend",HttpStatus.OK);
        }

        return new ResponseEntity<>("Sended",HttpStatus.OK);
    }

    @GetMapping("/search_friend")
    public ResponseEntity<?> searchFriend(HttpServletRequest request, @RequestParam String name){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        List<User> users = userService.searchFriend(userId,name);
        List<UserDto> userDtos = users.stream().map( i -> userMapper.mapTo(i)).collect(Collectors.toList());
        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }

    @GetMapping("/all_friend")
    public ResponseEntity<?> getAllFriend(HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        List<User> users = userService.getAllFriendByUserId(userId);
        List<UserDto> userDtos = users.stream().map( i -> userMapper.mapTo(i)).collect(Collectors.toList());
        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }

}
