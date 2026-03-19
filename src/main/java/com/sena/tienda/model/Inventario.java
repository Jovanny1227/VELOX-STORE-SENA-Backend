package com.sena.tienda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "Inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bicicleta", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Bicicleta bicicleta;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    public Inventario() {}

    public Inventario(Bicicleta bicicleta, Integer cantidadDisponible) {
        this.bicicleta = bicicleta;
        this.cantidadDisponible = cantidadDisponible;
    }

    public Long getIdInventario() { return idInventario; }
    public void setIdInventario(Long v) { this.idInventario = v; }
    public Bicicleta getBicicleta() { return bicicleta; }
    public void setBicicleta(Bicicleta v) { this.bicicleta = v; }
    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer v) { this.cantidadDisponible = v; }
}
