package com.example.demo.request;

import lombok.Data;

@Data
public class ChangeUserPassword {
    private String token;

    private String password;
}
