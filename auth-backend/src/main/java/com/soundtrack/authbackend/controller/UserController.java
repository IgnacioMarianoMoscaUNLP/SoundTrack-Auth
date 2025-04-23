package com.soundtrack.authbackend.controller;

import com.soundtrack.authbackend.dto.UserDTO;
import com.soundtrack.authbackend.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @RequestMapping("/me")
    public ResponseEntity<UserDTO>current(){
        return ResponseEntity.ok(new UserDTO());
    }
}
