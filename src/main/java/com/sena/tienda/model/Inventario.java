package com.sena.tienda.model;

import jakarta.persistence.*;

/**
 * ENTIDAD: Inventario
 * Tabla: Inventario
 *
 * Columnas según diagrama:
 *   id_inventario       BIGINT  PK AUTO_INCREMENT
 *   id_bicicleta        BIGINT  FK → bicicleta.id_bicicleta
 *   cantidad_disponible INT
 */
@Entity
@Table(name = "Inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    /**
     * @OneToOne: Un inventario pertenece a UNA bicicleta.
     * @JoinColumn genera la columna FK "id_bicicleta".
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bicicleta", nullable = false, unique = true)
    private Bicicleta bicicleta;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    // ========================== CONSTRUCTORES ==========================

    public Inventario() {}

    public Inventario(Bicicleta bicicleta, Integer cantidadDisponible) {
        this.bicicleta          = bicicleta;
        this.cantidadDisponible = cantidadDisponible;
    }

    // ========================== GETTERS Y SETTERS ==========================

    public Long getIdInventario()                  { return idInventario; }
    public void setIdInventario(Long v)            { this.idInventario = v; }

    public Bicicleta getBicicleta()                { return bicicleta; }
    public void setBicicleta(Bicicleta v)          { this.bicicleta = v; }

    public Integer getCantidadDisponible()         { return cantidadDisponible; }
    public void setCantidadDisponible(Integer v)   { this.cantidadDisponible = v; }

    @Override
    public String toString() {
        return "Inventario{idInventario=" + idInventario +
                ", bicicleta=" + (bicicleta != null ? bicicleta.getCodigo() : "null") +
                ", cantidadDisponible=" + cantidadDisponible + "}";
    }
}