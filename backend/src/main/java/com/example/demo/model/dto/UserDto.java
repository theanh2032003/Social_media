package com.example.demo.model.dto;

import com.example.demo.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;

    private String email;

    private String fullname;

    private String textId;

    private String avatar;

    private String banner;

    private String bio;

    private String location;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullname = user.getFullname();
        this.textId = user.getTextId();
        this.avatar = user.getAvatar();
        this.banner = user.getBanner();
        this.bio = user.getBio();
        this.location = user.getLocation();
    }


}
