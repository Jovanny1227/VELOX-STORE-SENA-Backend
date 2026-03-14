package com.sena.tienda.model;

import jakarta.persistence.*;

/**
 * ENTIDAD: Cliente
 * Tabla: Clientes  (nombre exacto del diagrama con C mayúscula)
 *
 * Columnas según diagrama:
 *   cliente_id  BIGINT       PK AUTO_INCREMENT
 *   documento   VARCHAR(20)
 *   nombre      VARCHAR(100)
 *   telefono    VARCHAR(20)
 */
@Entity
@Table(name = "Clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "telefono", length = 20)
    private String telefono;

    // ========================== CONSTRUCTORES ==========================

    public Cliente() {}

    public Cliente(String documento, String nombre, String telefono) {
        this.documento = documento;
        this.nombre    = nombre;
        this.telefono  = telefono;
    }

    // ========================== GETTERS Y SETTERS ==========================

    public Long getClienteId()             { return clienteId; }
    public void setClienteId(Long v)       { this.clienteId = v; }

    public String getDocumento()           { return documento; }
    public void setDocumento(String v)     { this.documento = v; }

    public String getNombre()              { return nombre; }
    public void setNombre(String v)        { this.nombre = v; }

    public String getTelefono()            { return telefono; }
    public void setTelefono(String v)      { this.telefono = v; }

    @Override
    public String toString() {
        return "Cliente{clienteId=" + clienteId +
                ", documento='" + documento +
                "', nombre='" + nombre +
                "', telefono='" + telefono + "'}";
    }
}