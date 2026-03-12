package com.sena.tienda.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD: Venta
 * Tabla: venta
 *
 * Columnas según diagrama:
 *   id_venta    BIGINT        PK AUTO_INCREMENT
 *   cliente_id  BIGINT        FK → Clientes.cliente_id
 *   fecha       DATETIME
 *   total       DECIMAL(10,2)
 */
@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    /**
     * @ManyToOne: Muchas ventas pueden pertenecer a UN cliente.
     * @JoinColumn genera la columna FK "cliente_id" en la tabla venta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    /**
     * @OneToMany: Una venta tiene MUCHOS detalles.
     * cascade = ALL: guardar/eliminar la venta propaga a sus detalles.
     */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    // ========================== CONSTRUCTORES ==========================

    public Venta() {}

    public Venta(Cliente cliente) {
        this.cliente = cliente;
        this.fecha   = LocalDateTime.now();
        this.total   = BigDecimal.ZERO;
    }

    // ========================== GETTERS Y SETTERS ==========================

    public Long getIdVenta()                       { return idVenta; }
    public void setIdVenta(Long v)                 { this.idVenta = v; }

    public Cliente getCliente()                    { return cliente; }
    public void setCliente(Cliente v)              { this.cliente = v; }

    public LocalDateTime getFecha()                { return fecha; }
    public void setFecha(LocalDateTime v)          { this.fecha = v; }

    public BigDecimal getTotal()                   { return total; }
    public void setTotal(BigDecimal v)             { this.total = v; }

    public List<DetalleVenta> getDetalles()        { return detalles; }
    public void setDetalles(List<DetalleVenta> v)  { this.detalles = v; }

    @Override
    public String toString() {
        return "Venta{idVenta=" + idVenta +
                ", cliente=" + (cliente != null ? cliente.getNombre() : "null") +
                ", fecha=" + fecha +
                ", total=" + total + "}";
    }
}