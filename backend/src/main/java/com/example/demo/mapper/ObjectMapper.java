package com.example.demo.mapper;

public interface ObjectMapper<A,B> {

    B mapTo(A a);
    A mapFrom(B b);
}
