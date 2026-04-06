package com.sena.tienda.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VentaDTO(
        Long idVenta,
        String nombreUsuario, // Actualizado de nombreCliente a nombreUsuario
        LocalDateTime fecha,
        BigDecimal total,
        List<DetalleVentaDTO> detalles
) {}