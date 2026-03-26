package com.sena.tienda.dto.response;

import java.math.BigDecimal;

public record DetalleVentaDTO(Integer cantidad, BigDecimal subtotal, BicicletaVentaDTO bicicleta) {}