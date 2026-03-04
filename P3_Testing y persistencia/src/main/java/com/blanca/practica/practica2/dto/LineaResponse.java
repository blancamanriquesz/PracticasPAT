package com.blanca.practica.practica2.dto;

public class LineaResponse {
    private Long idLinea;
    private Long idCarrito;
    private Long idArticulo;
    private Double precioUnitario;
    private Integer unidades;
    private Double costeLinea;

    public LineaResponse(Long idLinea, Long idCarrito, Long idArticulo,
                         Double precioUnitario, Integer unidades, Double costeLinea) {
        this.idLinea = idLinea;
        this.idCarrito = idCarrito;
        this.idArticulo = idArticulo;
        this.precioUnitario = precioUnitario;
        this.unidades = unidades;
        this.costeLinea = costeLinea;
    }

    public Long getIdLinea() { return idLinea; }
    public Long getIdCarrito() { return idCarrito; }
    public Long getIdArticulo() { return idArticulo; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public Integer getUnidades() { return unidades; }
    public Double getCosteLinea() { return costeLinea; }
}