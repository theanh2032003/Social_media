package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.ConfirmToken;
import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.ConfirmTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserDetailsService,UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @Override
    public String signUp(User user) {
        boolean isEmailExist = userRepository.findByEmail(user.getEmail()).isPresent();
        if(isEmailExist) {
            throw new IllegalAccessError("email is exist");
        }

        userRepository.save(user);

        String token = tokenService.createToken(user);

        return token;
    }

    @Override
    public void updateEnableUser(String email) {
        // TODO Auto-generated method stub
        userRepository.updateEnableUser(email);
    }


    @Override
    public String changePassword(String email) {

        Optional<User> existUser = userRepository.findByEmail(email);
        if(existUser.isEmpty()){
            throw  new IllegalArgumentException("user is not found");
        }

        String token = tokenService.createToken(existUser.get());

        return token;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public boolean existByTextId(String textId) {
        return userRepository.existsByTextId(textId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void changeInfo(User user, String avatar, String banner, String fullname, String bio, String location) {

        if(avatar!= null && avatar != "" && avatar != user.getAvatar() ){
            user.setAvatar(avatar);
        }

        if(banner!= null && banner != "" && banner != user.getBanner() ){
            user.setBanner(banner);
        }

        if(fullname!= null && fullname != "" && fullname != user.getFullname() ){
            user.setFullname(fullname);
        }

        if(bio!= null && bio != user.getBio() ){
            user.setBio(bio);
        }

        if(location!= null && location != user.getLocation() ){
            user.setLocation(location);
        }
        userRepository.save(user);


    }

    @Override
    public List<User> searchFriend(Long userId, String name) {
        return userRepository.searchUsersInListFriend(name,userId);
    }

    @Override
    public List<User> getAllFriendByUserId(Long userId) {
        return userRepository.getAllFriend(userId);
    }

    @Override
    public List<UserDto> searchUser(String name,Long userId) {
        // TODO Auto-generated method stub
        List<User> users = userRepository.searchUsers(name,userId);
        List<UserDto> userDtos = users.stream().map(i -> new UserDto(i)).collect(Collectors.toList());
        return userDtos;
    }


}
