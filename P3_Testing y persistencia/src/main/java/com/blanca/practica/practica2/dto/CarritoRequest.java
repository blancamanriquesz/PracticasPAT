package com.blanca.practica.practica2.dto;

public class CarritoRequest {
    private Long idUsuario;
    private String correoUsuario;

    public CarritoRequest() {}

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getCorreoUsuario() { return correoUsuario; }
    public void setCorreoUsuario(String correoUsuario) { this.correoUsuario = correoUsuario; }

}
