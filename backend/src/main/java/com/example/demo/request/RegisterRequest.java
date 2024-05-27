package com.example.demo.request;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String email;

    private String password;

    private String fullname;

    private Set<String> roles = new HashSet<>();

    private String textId;


}
