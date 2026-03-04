package com.blanca.practica.practica2.dto;

import java.util.List;

public class CarritoResponse {
    private Long idCarrito;
    private Long idUsuario;
    private String correoUsuario;
    private Double precioFinal;
    private List<LineaResponse> lineas;

    public CarritoResponse(Long idCarrito, Long idUsuario, String correoUsuario,
                           Double precioFinal, List<LineaResponse> lineas) {
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.correoUsuario = correoUsuario;
        this.precioFinal = precioFinal;
        this.lineas = lineas;
    }

    public Long getIdCarrito() { return idCarrito; }
    public Long getIdUsuario() { return idUsuario; }
    public String getCorreoUsuario() { return correoUsuario; }
    public Double getPrecioFinal() { return precioFinal; }
    public List<LineaResponse> getLineas() { return lineas; }
}