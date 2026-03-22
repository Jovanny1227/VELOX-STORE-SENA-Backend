package com.sena.tienda.dto.request;

import com.sena.tienda.model.TipoBicicleta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BicicletaRequest(
        @NotBlank(message = "El modelo es obligatorio") String modelo,
        @NotBlank(message = "La marca es obligatoria") String marca,
        @NotNull(message = "El precio es obligatorio") @Positive(message = "El precio debe ser mayor a 0") BigDecimal precio,
        @NotNull(message = "El tipo es obligatorio") TipoBicicleta tipo,
        @NotNull(message = "El ID del proveedor es obligatorio") Long proveedorId
) {}