package com.sena.tienda.dto.auth;

import com.sena.tienda.model.Rol;

public class AuthResponseDTO {
    private String token;
    private String nombre;
    private Rol rol;

    public AuthResponseDTO(String token, String nombre, Rol rol) {
        this.token = token;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters
    public String getToken() { return token; }
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
}