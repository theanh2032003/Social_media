package com.example.demo.response;

import com.example.demo.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponse {
    private String type;

    private Message message;


}
