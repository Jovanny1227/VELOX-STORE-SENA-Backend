package com.sena.tienda.dto.request;

import java.util.List;

public class VentaRequest {

    private Long usuarioId; // Actualizado de clienteId a usuarioId
    private String codigoBicicleta;
    private int cantidad;
    private List<ItemVentaRequest> items;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getCodigoBicicleta() { return codigoBicicleta; }
    public void setCodigoBicicleta(String codigoBicicleta) { this.codigoBicicleta = codigoBicicleta; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public List<ItemVentaRequest> getItems() { return items; }
    public void setItems(List<ItemVentaRequest> items) { this.items = items; }

    public static class ItemVentaRequest {
        private String codigoBicicleta;
        private int cantidad;

        public String getCodigoBicicleta() { return codigoBicicleta; }
        public void setCodigoBicicleta(String codigoBicicleta) { this.codigoBicicleta = codigoBicicleta; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}