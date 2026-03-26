package com.sena.tienda.dto.request;

import java.util.List;

public class VentaRequest {

    private Long clienteId;
    private String codigoBicicleta;
    private int cantidad;
    private List<ItemVentaRequest> items;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long c) { this.clienteId = c; }
    public String getCodigoBicicleta() { return codigoBicicleta; }
    public void setCodigoBicicleta(String c) { this.codigoBicicleta = c; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int c) { this.cantidad = c; }
    public List<ItemVentaRequest> getItems() { return items; }
    public void setItems(List<ItemVentaRequest> i) { this.items = i; }

    public static class ItemVentaRequest {
        private String codigoBicicleta;
        private int cantidad;
        public String getCodigoBicicleta() { return codigoBicicleta; }
        public void setCodigoBicicleta(String c) { this.codigoBicicleta = c; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int c) { this.cantidad = c; }
    }
}
