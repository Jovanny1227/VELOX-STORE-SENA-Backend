package com.sena.tienda.dto.request;

import com.sena.tienda.model.TipoVenta;
import java.util.List;

public class VentaPresencialRequest {
    private Long usuarioId;
    private List<VentaRequest.ItemVentaRequest> items;
    private Long clienteId;
    private TipoVenta tipoVenta;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public List<VentaRequest.ItemVentaRequest> getItems() { return items; }
    public void setItems(List<VentaRequest.ItemVentaRequest> items) { this.items = items; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public TipoVenta getTipoVenta() { return tipoVenta; }
    public void setTipoVenta(TipoVenta tipoVenta) { this.tipoVenta = tipoVenta; }
}