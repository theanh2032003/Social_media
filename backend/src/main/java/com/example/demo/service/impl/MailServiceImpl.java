package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.model.ConfirmToken;
import com.example.demo.model.User;
import com.example.demo.repository.ConfirmTokenRepository;
import com.example.demo.service.MailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ConfirmTokenRepository tokenRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;



    @Override
    public void send(String to, String email) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setText(email);
            helper.setFrom("dinhanh2032003@gmail.com");
            helper.setSubject("Confirmed email");
            helper.setTo(to);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new IllegalStateException("email is not exist");
        }

    }



    @Override
    public String buildEmail(String name, String content) {
        String email =  "This is token: " + content +" ,"+name;

        return email;
    }

    @Override
    public String confirmTokenToRegister(String token) {
        Optional<ConfirmToken> existToken = tokenRepository.findByToken(token);
        if(existToken.isEmpty()) {
            throw new IllegalAccessError("token isn't exist");
        }

        if(existToken.get().getExpiresAt().isBefore(LocalDateTime.now()) ) {
            throw new IllegalAccessError("token is out of time");
        }

        tokenRepository.updateConfirmToken(token, LocalDateTime.now());
        userRepository.updateEnableUser(existToken.get().getUser().getEmail());

        return "confirm";
    }

    @Override
    public String confirmTokenToChangePassword(String token,String encodePassword, String email) {
        Optional<ConfirmToken> existToken = tokenRepository.findByToken(token);
        if(existToken.isEmpty()) {
            throw new IllegalAccessError("token isn't exist");
        }

        if(existToken.get().getExpiresAt().isBefore(LocalDateTime.now()) ) {
            throw new IllegalAccessError("token is out of time");
        }
        Optional<User> existUser = userRepository.findByEmail(email);
        if(existUser.isEmpty()){
            throw new IllegalArgumentException("user not found");
        }
        existUser.get().setPassword(encodePassword);
        userRepository.save(existUser.get());
        tokenRepository.updateConfirmToken(token, LocalDateTime.now());

        return "confirm";
    }

}
