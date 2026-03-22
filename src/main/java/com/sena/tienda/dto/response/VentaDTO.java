package com.sena.tienda.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VentaDTO(Long idVenta, String nombreCliente, LocalDateTime fecha, BigDecimal total) {}