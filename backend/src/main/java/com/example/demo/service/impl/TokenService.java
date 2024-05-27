package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ConfirmToken;
import com.example.demo.repository.ConfirmTokenRepository;

@Service
public class TokenService {

    @Autowired
    private ConfirmTokenRepository cfmRepository;

    @Autowired
    private ConfirmTokenRepository tokenRepository;

    public ConfirmToken saveConfirmToken(ConfirmToken confirmToken) {
        return cfmRepository.save(confirmToken);
    }

    public ConfirmToken getCfTokenByToken(String token) {
        Optional<ConfirmToken> existCfToken = cfmRepository.findByToken(token);
        if(existCfToken.isEmpty()) {
            return null;
        }
        return existCfToken.get();
    }

    public String createToken(User user){
        String token = UUID.randomUUID().toString().replace("/","");
        tokenRepository.save(new ConfirmToken(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), token, user));
        return token;
    }

    public Optional<ConfirmToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
