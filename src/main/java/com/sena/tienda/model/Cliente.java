package com.sena.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ENTIDAD: Cliente
 * Tabla: Clientes
 */
@Entity
@Table(name = "Clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede superar 20 caracteres")
    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    // ========================== CONSTRUCTORES ==========================

    public Cliente() {}

    public Cliente(String documento, String nombre, String telefono) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // ========================== GETTERS Y SETTERS ==========================

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "Cliente{" +
                "clienteId=" + clienteId +
                ", documento='" + documento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
