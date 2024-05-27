package com.example.demo;

import com.example.demo.model.ERole;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

@DataJpaTest
public class UserTestJpa {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestData data;
    @Test
    public void testThatUserCanBeCreated(){
        User user = User.builder()
                .email("anh@gmail.com")
                .fullname("the anh")
                .enable(true)
                .locked(false)
                .password("123")
                .posts(null)
                .roles(Set.of(roleRepository.findByName(ERole.ROLE_USER)))
                .comments(null)
                .textId("@anh")
                .build();
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assert (users.size() ==3);
    }
}
