package com.sena.tienda.dto.request;

public class VentaRequest {

    private Long clienteId;
    private String codigoBicicleta;
    private int cantidad;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getCodigoBicicleta() { return codigoBicicleta; }
    public void setCodigoBicicleta(String codigoBicicleta) { this.codigoBicicleta = codigoBicicleta; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
