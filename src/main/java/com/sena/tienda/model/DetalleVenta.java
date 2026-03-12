package com.sena.tienda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * ENTIDAD: DetalleVenta
 * Tabla: detalle_venta
 *
 * Columnas según diagrama:
 *   id_detalle_venta  BIGINT        PK AUTO_INCREMENT
 *   id_venta          BIGINT        FK → venta.id_venta
 *   id_bicicleta      BIGINT        FK → bicicleta.id_bicicleta
 *   cantidad          INT
 *   subtotal          DECIMAL(10,2)
 */
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long idDetalleVenta;

    /**
     * @ManyToOne → Venta (lado dueño de la relación bidireccional)
     * Genera columna FK "id_venta"
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    /**
     * @ManyToOne → Bicicleta
     * Genera columna FK "id_bicicleta"
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;  // Calculado: precio × cantidad

    // ========================== CONSTRUCTORES ==========================

    public DetalleVenta() {}

    public DetalleVenta(Venta venta, Bicicleta bicicleta, Integer cantidad) {
        this.venta     = venta;
        this.bicicleta = bicicleta;
        this.cantidad  = cantidad;
        // Cálculo automático del subtotal
        this.subtotal  = bicicleta.getPrecio().multiply(new BigDecimal(cantidad));
    }

    // ========================== GETTERS Y SETTERS ==========================

    public Long getIdDetalleVenta()              { return idDetalleVenta; }
    public void setIdDetalleVenta(Long v)        { this.idDetalleVenta = v; }

    public Venta getVenta()                      { return venta; }
    public void setVenta(Venta v)                { this.venta = v; }

    public Bicicleta getBicicleta()              { return bicicleta; }
    public void setBicicleta(Bicicleta v)        { this.bicicleta = v; }

    public Integer getCantidad()                 { return cantidad; }
    public void setCantidad(Integer v)           { this.cantidad = v; }

    public BigDecimal getSubtotal()              { return subtotal; }
    public void setSubtotal(BigDecimal v)        { this.subtotal = v; }

    @Override
    public String toString() {
        return "DetalleVenta{idDetalleVenta=" + idDetalleVenta +
                ", bicicleta=" + (bicicleta != null ? bicicleta.getCodigo() : "null") +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal + "}";
    }
}