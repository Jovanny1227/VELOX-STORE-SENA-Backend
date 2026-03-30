package com.sena.tienda.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sena.tienda.dto.response.DashboardDTO;
import com.sena.tienda.model.MovimientoInventario;
import com.sena.tienda.model.Venta;
import com.sena.tienda.repository.BicicletaRepository;
import com.sena.tienda.repository.InventarioRepository;
import com.sena.tienda.repository.MovimientoInventarioRepository;
import com.sena.tienda.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;

    public ReporteService(VentaRepository ventaRepository, MovimientoInventarioRepository movimientoRepository,
                          BicicletaRepository bicicletaRepository, InventarioRepository inventarioRepository) {
        this.ventaRepository = ventaRepository;
        this.movimientoRepository = movimientoRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
    }

    public DashboardDTO obtenerMetricasDashboard() {
        List<Venta> ventas = ventaRepository.findAll();
        BigDecimal ingresosTotales = ventas.stream().map(Venta::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalBicicletas = (int) bicicletaRepository.count();
        Integer stockTotal = inventarioRepository.stockTotal();

        return new DashboardDTO(ingresosTotales, ventas.size(), totalBicicletas, stockTotal != null ? stockTotal : 0);
    }

    // -----------------------------------------------------------------------------------------
    // REPORTE DE VENTAS - MEJORADO
    // -----------------------------------------------------------------------------------------
    public byte[] generarReporteVentasPdf() {
        Document document = new Document(PageSize.A4); // Formato Vertical
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Estilos de fuentes
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font fontDatos = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);

            // Cabecera del Documento
            Paragraph titulo = new Paragraph("VELOX STORE", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Reporte Oficial de Ventas Generales\nFecha de emisión: " + java.time.LocalDate.now() + "\n\n", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitulo);

            // Tabla de 4 columnas
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 4f, 3f, 2.5f});

            // Encabezados Azules
            String[] cabeceras = {"N° Orden", "Cliente", "Fecha y Hora", "Total Pagado"};
            for (String cabecera : cabeceras) {
                PdfPCell cell = new PdfPCell(new Phrase(cabecera, fontCabecera));
                cell.setBackgroundColor(new BaseColor(33, 106, 191)); // Azul Corporativo
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(10);
                table.addCell(cell);
            }

            // Llenado de Datos
            List<Venta> ventas = ventaRepository.findAll();
            BigDecimal granTotal = BigDecimal.ZERO;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (Venta v : ventas) {
                table.addCell(new PdfPCell(new Phrase("# " + v.getIdVenta(), fontDatos)));
                table.addCell(new PdfPCell(new Phrase(v.getUsuario().getNombre(), fontDatos)));
                table.addCell(new PdfPCell(new Phrase(v.getFecha().format(formatter), fontDatos)));

                PdfPCell cellTotal = new PdfPCell(new Phrase("$ " + v.getTotal(), fontDatos));
                cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cellTotal);

                granTotal = granTotal.add(v.getTotal());
            }
            document.add(table);

            // Resumen Final
            Paragraph resumen = new Paragraph("\nTotal de Ingresos Recaudados: $ " + granTotal, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK));
            resumen.setAlignment(Element.ALIGN_RIGHT);
            document.add(resumen);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
        return out.toByteArray();
    }

    // -----------------------------------------------------------------------------------------
    // REPORTE DE MOVIMIENTOS (INVENTARIO) - MEJORADO Y DETALLADO
    // -----------------------------------------------------------------------------------------
    public byte[] generarReporteMovimientosPdf() {
        // Formato Horizontal (Landscape) para que quepan más detalles
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font fontDatos = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

            Paragraph titulo = new Paragraph("VELOX STORE", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Reporte Detallado de Movimientos de Inventario\nFecha de emisión: " + java.time.LocalDate.now() + "\n\n", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitulo);

            // Tabla de 8 Columnas
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.8f, 1.8f, 1.5f, 3.5f, 1f, 1.8f, 2f, 3f});

            // Encabezados Naranjas
            String[] cabeceras = {"Fecha", "Tipo Mov.", "Código", "Bicicleta (Marca y Modelo)", "Cant", "Valor Unid.", "Total Movimiento", "Observaciones"};
            for (String cabecera : cabeceras) {
                PdfPCell cell = new PdfPCell(new Phrase(cabecera, fontCabecera));
                cell.setBackgroundColor(new BaseColor(220, 83, 30)); // Naranja Velox
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                table.addCell(cell);
            }

            List<MovimientoInventario> movimientos = movimientoRepository.findAll();

            for (MovimientoInventario m : movimientos) {
                table.addCell(new PdfPCell(new Phrase(m.getFecha().toLocalDate().toString(), fontDatos)));

                // Darle color al tipo de movimiento (Ej. ENTRADA = Verde, SALIDA = Rojo)
                Font fontTipo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8,
                        m.getTipo().name().contains("ENTRADA") ? new BaseColor(34, 139, 34) : new BaseColor(178, 34, 34));
                PdfPCell cellTipo = new PdfPCell(new Phrase(m.getTipo().name(), fontTipo));
                cellTipo.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTipo);

                table.addCell(new PdfPCell(new Phrase(m.getBicicleta().getCodigo(), fontDatos)));
                table.addCell(new PdfPCell(new Phrase(m.getBicicleta().getMarca() + " " + m.getBicicleta().getModelo(), fontDatos)));

                PdfPCell cellCant = new PdfPCell(new Phrase(String.valueOf(m.getCantidad()), fontDatos));
                cellCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellCant);

                // Cálculos financieros del movimiento
                BigDecimal precioUnitario = m.getPrecioUnitario() != null ? m.getPrecioUnitario() : BigDecimal.ZERO;
                BigDecimal totalMovimiento = precioUnitario.multiply(new BigDecimal(m.getCantidad()));

                table.addCell(new PdfPCell(new Phrase("$ " + precioUnitario, fontDatos)));
                table.addCell(new PdfPCell(new Phrase("$ " + totalMovimiento, fontDatos)));
                table.addCell(new PdfPCell(new Phrase(m.getObservacion(), fontDatos)));
            }
            document.add(table);

            // Resumen de la tabla
            Paragraph resumen = new Paragraph("\nTotal de registros históricos: " + movimientos.size(), fontSubtitulo);
            document.add(resumen);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF de Inventario", e);
        }

        return out.toByteArray();
    }
}