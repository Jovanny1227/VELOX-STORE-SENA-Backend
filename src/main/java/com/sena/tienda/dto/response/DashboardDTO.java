package com.sena.tienda.dto.response;

import java.math.BigDecimal;

public record DashboardDTO(
        BigDecimal ingresosTotales,
        int cantidadVentas,
        int totalBicicletasCatalogo,
        int stockTotalInventario
) {}