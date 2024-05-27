package com.example.demo.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.example.demo.encryption.PasswordEncryption;
import com.example.demo.model.ConfirmToken;
import com.example.demo.request.ChangeUserPassword;
import com.example.demo.response.LoginResponse;
import com.example.demo.security.jwt.JwtFilter;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.impl.MailServiceImpl;
import com.example.demo.service.impl.RegisterServiceImpl;
import com.example.demo.service.impl.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RegisterServiceImpl registerService;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TokenService tokenService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()) );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtProvider.createJwt(authentication);
        Long userId = jwtProvider.getIdByJwtToken(jwtToken);
        System.out.println(jwtToken);
        return new ResponseEntity<>(new LoginResponse(jwtToken,userId),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest ){
        if(userService.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("email is already taken",HttpStatus.BAD_REQUEST);
        }

        if(userService.existByTextId(registerRequest.getTextId())) {
            return new ResponseEntity<>("textId is already taken",HttpStatus.BAD_REQUEST);
        }

        registerService.register(registerRequest);

        return new ResponseEntity<>(registerRequest.getEmail(),HttpStatus.OK);
    }

    @GetMapping("/register/confirm")
    public String confirm(@RequestParam String token) {
        return	mailService.confirmTokenToRegister(token);

    }

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String email){


        Optional<User> user = userService.findByEmail(email.toString());
        System.out.println(user);
        System.out.println(email.toString());
        if(user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String token = userService.changePassword(user.get().getEmail());
        mailService.send(user.get().getEmail(),mailService.buildEmail(userService.findByEmail(user.get().getEmail()).get().getFullname(),token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/changePassword/confirm")
    public ResponseEntity<?> changePassword(@RequestParam("email") String email,
                                            @RequestParam("token") String token, @RequestParam("password") String password) {


        Optional<ConfirmToken> confirmToken = tokenService.findByToken(token);
        if(confirmToken.isEmpty()){
            return new ResponseEntity<>("Không tìm thấy token",HttpStatus.NOT_FOUND);
        }
        String encodePassword = passwordEncoder.encode(password);

        return new ResponseEntity<>(mailService.confirmTokenToChangePassword(token,encodePassword,email),HttpStatus.OK)	;

    }

    @PostMapping("/re_send_token")
    public ResponseEntity<?> reSendConfirmToken(@RequestParam("email") String email){

        Optional<User> user = userService.findByEmail(email);
        if(user.isEmpty()){
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }

        String token = tokenService.createToken(user.get());

        mailService.send(user.get().getEmail(),mailService.buildEmail(user.get().getFullname(),token));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
