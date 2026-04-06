package com.sena.tienda.dto.auth;

import com.sena.tienda.model.Rol;

public class AuthResponseDTO {
    private String token;
    private Long id; // <--- NUEVO CAMPO
    private String nombre;
    private Rol rol;

    // Constructor actualizado
    public AuthResponseDTO(String token, Long id, String nombre, Rol rol) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters
    public String getToken() { return token; }
    public Long getId() { return id; } // <--- NUEVO GETTER
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
}