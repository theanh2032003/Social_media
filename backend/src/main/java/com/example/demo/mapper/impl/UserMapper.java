package com.example.demo.mapper.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.mapper.ObjectMapper;
import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements ObjectMapper<User, UserDto > {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDto mapTo(User a) {
        // TODO Auto-generated method stub
        return new UserDto(a);
    }

    @Override
    public User mapFrom(UserDto b) {
        // TODO Auto-generated method stub
        Optional<User>  user = userRepo.findById(b.getId());
        if(user.isEmpty()) {
            return null;
        }
        return user.get();
    }

}
