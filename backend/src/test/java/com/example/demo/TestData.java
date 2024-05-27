package com.example.demo;

import com.example.demo.model.ERole;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class TestData {

    @Autowired
    private RoleRepository roleRepository;

    public User user1(){
        return User.builder()
                .email("anh@gmail.com")
                .fullname("the anh")
                .enable(true)
                .locked(false)
                .password("123")
                .posts(null)
                .roles(Set.of(roleRepository.findByName(ERole.ROLE_USER)))
                .comments(null)
                .textId("@anh")
                .build();
    }

    public User user2(){
        return User.builder()
                .email("anh2@gmail.com")
                .fullname("the anh 2")
                .enable(true)
                .locked(false)
                .password("123")
                .posts(null)
                .roles(Set.of(roleRepository.findByName(ERole.ROLE_USER)))
                .comments(null)
                .textId("@anh2")
                .build();
    }

    public Post post1(){
        return new Post().builder()
                .user(this.user1())
                .createAt(LocalDateTime.now())
                .content("number 1")
                .comments(null)
                .build();
    }


}
