package com.example.demo.service;


import com.example.demo.model.User;

public interface MailService {

    void send(String to, String email);

//    String sendConfirmToken(String link, User user);

    String buildEmail(String name, String content);

    String confirmTokenToRegister(String token);

    String confirmTokenToChangePassword(String token,String password,String email);
}