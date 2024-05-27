package com.example.demo.controller;


import com.example.demo.file.FileUpLoad;
import com.example.demo.model.GroupChat;
import com.example.demo.model.Message;
import com.example.demo.response.CallResponse;
import com.example.demo.response.ChatResponse;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.impl.MessageServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private FileUpLoad fileUpLoad;

    private final String PATH_IMAGE = "D:/Tai_lieu_PTIT/ttcs/Frontend/mxh/public/groupchat/";


    @GetMapping("/get/{user1Id}")
    public ResponseEntity<?>  getMessagesOfChatRoom(@PathVariable Long user1Id, HttpServletRequest request){

        Long user2Id = jwtProvider.getIdFromHttpRequest(request);
        Optional<GroupChat> groupchat = messageService.findGroupChatByUser1IdAndUser2Id(user1Id,user2Id);

        if(groupchat.isEmpty()){

            return new ResponseEntity<>(null,HttpStatus.OK);
        }
        List<Message> messages = messageService.getAllMessageByGroupChatId(groupchat.get().getId());
        Collections.sort(messages, Comparator.comparing(Message::getCreateAt));
        Collections.reverse(messages);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping(value = "/send_message",produces= MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sendMessage(@RequestParam(name = "images", required = false)MultipartFile[] images, @RequestParam("sender") Long senderId, @RequestParam("receiver") Long receiverId,
                                         @RequestParam("content") String content) throws IOException {

        Message message = messageService.sendMessage(receiverId,senderId,content);
        if(images !=null){
            List<String> strImages = fileUpLoad.saveFile(PATH_IMAGE, String.valueOf(message.getGroupChat().getId()),images);
            List<String> subImages = strImages.stream().map(image -> image.substring(image.indexOf("\\groupchat"))).collect(Collectors.toList());
            message.setImages(subImages);
            messageService.save(message);
        }

        ChatResponse chatResponse = new ChatResponse("new message",message);
        String topic = "/topic/groupchat/" +((senderId > receiverId) ? receiverId :senderId).toString()+"_"+((senderId < receiverId) ? receiverId :senderId).toString();
        System.out.println(images);
        messagingTemplate.convertAndSend( topic,chatResponse);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PatchMapping("/change_content")
    public ResponseEntity<?> changeContentMessage(@RequestParam Long messageId, @RequestParam String content){
        Message message = messageService.changeMessage(messageId,content);

        messagingTemplate.convertAndSend("topic/groupchat/" + message.getGroupChat().getUser1().toString() + "_" + message.getGroupChat().getUser2().toString(), message);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PatchMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestParam Long messageId, @RequestParam Long senderId, @RequestParam Long receiverId){
        Message message = messageService.deleteMessage(messageId);
        String topic = "/topic/groupchat/" +((senderId > receiverId) ? receiverId :senderId).toString()+"_"+((senderId < receiverId) ? receiverId :senderId).toString();
        ChatResponse chatResponse = new ChatResponse("delete message",message);
        messagingTemplate.convertAndSend( topic,chatResponse);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PostMapping("/call_notification")
    public ResponseEntity<?> getCallNotification(@RequestBody CallResponse callResponse){
//        CallResponse callResponse = new CallResponse("",groupChatId);
        messagingTemplate.convertAndSend("/topic/call",callResponse);
        return new ResponseEntity<>(callResponse,HttpStatus.OK);
    }

}
