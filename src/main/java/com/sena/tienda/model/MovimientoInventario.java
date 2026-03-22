package com.sena.tienda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bicicleta", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Bicicleta bicicleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Proveedor proveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoMovimiento tipo;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "observacion", length = 255)
    private String observacion;

    public MovimientoInventario() {}

    public MovimientoInventario(Bicicleta bicicleta, Proveedor proveedor,
                                TipoMovimiento tipo, Integer cantidad,
                                BigDecimal precioUnitario, String observacion) {
        this.bicicleta = bicicleta;
        this.proveedor = proveedor;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.observacion = observacion;
        this.fecha = LocalDateTime.now();
    }

    public Long getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Long v) { this.idMovimiento = v; }
    public Bicicleta getBicicleta() { return bicicleta; }
    public void setBicicleta(Bicicleta v) { this.bicicleta = v; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor v) { this.proveedor = v; }
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento v) { this.tipo = v; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer v) { this.cantidad = v; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal v) { this.precioUnitario = v; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime v) { this.fecha = v; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String v) { this.observacion = v; }
}
