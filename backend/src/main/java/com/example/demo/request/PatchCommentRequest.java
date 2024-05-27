package com.example.demo.request;

import lombok.Data;

@Data
public class PatchCommentRequest {
    private Long id;
    private String content;
}
