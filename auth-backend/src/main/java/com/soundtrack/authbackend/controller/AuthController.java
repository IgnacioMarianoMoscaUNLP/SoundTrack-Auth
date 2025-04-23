package com.soundtrack.authbackend.controller;

import com.soundtrack.authbackend.dto.UserDTO;
import com.soundtrack.authbackend.entity.User;
import com.soundtrack.authbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO
                                                     user) {
        return ResponseEntity.ok(user);
    }
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }
}
