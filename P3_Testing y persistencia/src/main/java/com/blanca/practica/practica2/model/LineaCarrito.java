package com.blanca.practica.practica2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lineas_carrito")
public class LineaCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLinea;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    private Long idArticulo;
    private Double precioUnitario;
    private Integer unidades;
    private Double costeLinea;

    public LineaCarrito() {}

    public Long getIdLinea() { return idLinea; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }

    public Long getIdArticulo() { return idArticulo; }
    public void setIdArticulo(Long idArticulo) { this.idArticulo = idArticulo; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getUnidades() { return unidades; }
    public void setUnidades(Integer unidades) { this.unidades = unidades; }

    public Double getCosteLinea() { return costeLinea; }
    public void setCosteLinea(Double costeLinea) { this.costeLinea = costeLinea; }
}