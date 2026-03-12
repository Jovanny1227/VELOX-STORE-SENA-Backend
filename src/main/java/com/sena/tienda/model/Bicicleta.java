package com.sena.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bicicleta")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bicicleta")
    private Long idBicicleta;

    @Column(name = "codigo", unique = true, length = 50)
    private String codigo;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @NotBlank(message = "La marca es obligatoria")
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoBicicleta tipo;

    public Bicicleta() {}

    public Bicicleta(String modelo, String marca, BigDecimal precio, TipoBicicleta tipo) {
        this.modelo = modelo;
        this.marca = marca;
        this.precio = precio;
        this.tipo = tipo;
    }

    public Long getIdBicicleta() { return idBicicleta; }
    public void setIdBicicleta(Long idBicicleta) { this.idBicicleta = idBicicleta; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public TipoBicicleta getTipo() { return tipo; }
    public void setTipo(TipoBicicleta tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "Bicicleta{" +
                "id=" + idBicicleta +
                ", codigo='" + codigo + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipo=" + tipo +
                ", precio=" + precio +
                '}';
    }
}