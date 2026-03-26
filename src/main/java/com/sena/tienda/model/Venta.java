package com.sena.tienda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"venta"})
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}

    public Venta(Cliente cliente) {
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();
        this.total = BigDecimal.ZERO;
    }

    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long v) { this.idVenta = v; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente v) { this.cliente = v; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime v) { this.fecha = v; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal v) { this.total = v; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> v) { this.detalles = v; }
}
