package com.sena.tienda.dto.request;

import com.sena.tienda.model.TipoMovimiento;
import java.math.BigDecimal;

public class MovimientoRequest {

    private String codigoBicicleta;
    private Long idProveedor;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String observacion;

    public String getCodigoBicicleta() { return codigoBicicleta; }
    public void setCodigoBicicleta(String codigoBicicleta) { this.codigoBicicleta = codigoBicicleta; }

    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
