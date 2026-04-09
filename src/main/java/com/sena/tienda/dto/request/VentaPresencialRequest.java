package com.sena.tienda.dto.request;

import com.sena.tienda.model.TipoVenta;
import lombok.Data;
import java.util.List;

@Data
public class VentaPresencialRequest {
    private List<VentaRequest.ItemVentaRequest> items;
    private TipoVenta tipoVenta;
    private Long clienteId; // Agregamos explícitamente el ID del cliente
}
