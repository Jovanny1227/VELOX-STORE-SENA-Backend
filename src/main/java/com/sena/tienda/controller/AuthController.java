package com.sena.tienda.controller;

import com.sena.tienda.dto.auth.AuthRequestDTO;
import com.sena.tienda.dto.auth.AuthResponseDTO;
import com.sena.tienda.dto.auth.RegisterRequestDTO;
import com.sena.tienda.model.Rol;
import com.sena.tienda.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/registro-cliente")
    public ResponseEntity<AuthResponseDTO> registrarCliente(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(service.registrar(request, Rol.CLIENTE));
    }

    @PostMapping("/registro-admin")
    public ResponseEntity<AuthResponseDTO> registrarAdmin(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(service.registrar(request, Rol.ADMIN));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(service.login(request));
    }
}