package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.ConfirmToken;
import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ConfirmTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.request.RegisterRequest;
import com.example.demo.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService{

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private ConfirmTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(RegisterRequest registerRequest) {

        Set<Role> roles = new HashSet<>();


        if( registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add(roleRepo.findByName(ERole.ROLE_USER));
        }else {
            Set<String> autho = registerRequest.getRoles();
            autho.forEach(i -> {
                switch (i) {
                    case "user":
                        roles.add(roleRepo.findByName(ERole.ROLE_USER));
                        break;

                    default:
                        roles.add(roleRepo.findByName(ERole.ROLE_ADMIN));
                }
            });
        }



        User user = new User(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword())
                , registerRequest.getFullname(), registerRequest.getTextId(), roles, null, null);

        String token = userService.signUp(user);
//        String link = registerRequest.getRootLink() + token ;
        mailService.send(user.getEmail(),mailService.buildEmail(user.getFullname(), token));

        return token;
    }




}
