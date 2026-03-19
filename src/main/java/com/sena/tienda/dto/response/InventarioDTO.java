package com.sena.tienda.dto.response;

import com.sena.tienda.model.TipoBicicleta;
import java.math.BigDecimal;

public class InventarioDTO {

    private String codigo;
    private String marca;
    private String modelo;
    private TipoBicicleta tipo;
    private BigDecimal precio;
    private Integer stock;

    public InventarioDTO(String codigo, String marca, String modelo,
                         TipoBicicleta tipo, BigDecimal precio, Integer stock) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() { return codigo; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public TipoBicicleta getTipo() { return tipo; }
    public BigDecimal getPrecio() { return precio; }
    public Integer getStock() { return stock; }
}
