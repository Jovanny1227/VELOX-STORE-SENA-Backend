package com.sena.tienda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long idDetalleVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    @JsonIgnoreProperties({"detalles", "hibernateLazyInitializer", "handler"})
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bicicleta", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Bicicleta bicicleta;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public DetalleVenta() {}

    public DetalleVenta(Venta venta, Bicicleta bicicleta, Integer cantidad) {
        this.venta = venta;
        this.bicicleta = bicicleta;
        this.cantidad = cantidad;
        this.subtotal = bicicleta.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getIdDetalleVenta() { return idDetalleVenta; }
    public void setIdDetalleVenta(Long v) { this.idDetalleVenta = v; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta v) { this.venta = v; }
    public Bicicleta getBicicleta() { return bicicleta; }
    public void setBicicleta(Bicicleta v) { this.bicicleta = v; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer v) { this.cantidad = v; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal v) { this.subtotal = v; }
}
