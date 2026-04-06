package com.sena.tienda.controller;

import com.sena.tienda.dto.response.DashboardDTO;
import com.sena.tienda.service.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('ADMIN')") // Exclusivo para el Administrador
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // Ruta para llenar los datos numéricos del Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> obtenerMetricas() {
        return ResponseEntity.ok(reporteService.obtenerMetricasDashboard());
    }

    // Ruta para descargar el PDF de Ventas
    @GetMapping("/ventas/pdf")
    public ResponseEntity<byte[]> descargarReporteVentasPdf() {
        byte[] pdfBytes = reporteService.generarReporteVentasPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_ventas_velox.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // Ruta para descargar el PDF de Movimientos de Inventario
    @GetMapping("/movimientos/pdf")
    public ResponseEntity<byte[]> descargarReporteMovimientosPdf() {
        byte[] pdfBytes = reporteService.generarReporteMovimientosPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_inventario_velox.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}