package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;

public interface UserService {

    String signUp(User user);

    void updateEnableUser(String email);

    List<UserDto> searchUser(String name, Long userId);

    String changePassword(String email);

    User save(User user);

    void deleteUser();

    boolean existByTextId(String textId);

    boolean existsByEmail(String email);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    void changeInfo(User user, String avatar, String banner, String fullname, String bio, String location);

    List<User> searchFriend(Long userId, String name);

    List<User> getAllFriendByUserId(Long userId);

}
