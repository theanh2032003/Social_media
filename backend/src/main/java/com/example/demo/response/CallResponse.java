package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallResponse {
    private String type;
    private String message;
    private Long callerId;
}
