package com.soundtrack.authbackend.controller;

import com.soundtrack.authbackend.dto.UserDTO;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @GetMapping(value="/random")//, produces = MediaType.APPLICATION_JSON_VALUE)         // Endpoint de prueba
    public ResponseEntity<UserDTO> random() {
        return ResponseEntity.ok(new UserDTO());
    }

    
    @GetMapping("/profile")         // Perfil básico del usuario
    public ResponseEntity<UserDTO> getProfile() {
        return ResponseEntity.ok(new UserDTO());
    }

    @GetMapping("/preferences")     // Preferencias de la app
    public ResponseEntity<UserDTO> getPreferences() {
        return ResponseEntity.ok(new UserDTO());
    }

    @PutMapping("/preferences")     // Actualizar preferencias
    public ResponseEntity<UserDTO> updatePreferences(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userDTO);
    }
}

