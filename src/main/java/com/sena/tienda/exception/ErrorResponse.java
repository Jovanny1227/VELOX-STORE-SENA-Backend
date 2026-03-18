package com.sena.tienda.exception;

import java.time.LocalDateTime;

/**
 * Objeto que define la estructura del JSON que se enviará al frontend
 * cuando ocurra cualquier error.
 */
public class ErrorResponse {

    private LocalDateTime fechaHora;
    private int status;
    private String error;
    private String mensaje;

    public ErrorResponse(LocalDateTime fechaHora, int status, String error, String mensaje) {
        this.fechaHora = fechaHora;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}