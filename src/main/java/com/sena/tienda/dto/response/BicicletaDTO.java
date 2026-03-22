package com.sena.tienda.dto.response;

import com.sena.tienda.model.TipoBicicleta;
import java.math.BigDecimal;

public record BicicletaDTO(
        Long id,
        String codigo,
        String modelo,
        String marca,
        BigDecimal precio,
        TipoBicicleta tipo,
        String nombreProveedor
) {}