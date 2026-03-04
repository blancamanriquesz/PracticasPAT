package com.blanca.practica.practica2.controller;


import com.blanca.practica.practica2.dto.CarritoRequest;
import com.blanca.practica.practica2.dto.CarritoResponse;
import com.blanca.practica.practica2.dto.LineaRequest;
import com.blanca.practica.practica2.model.Carrito;
import com.blanca.practica.practica2.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Carrito> listar() {
        return service.listar();
    }

    @GetMapping("/{idCarrito}")
    public ResponseEntity<CarritoResponse> obtener(@PathVariable Long idCarrito) {
        return service.obtenerResponse(idCarrito)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Carrito> crear(@RequestBody CarritoRequest req) {
        Carrito creado = service.crear(req);
        return ResponseEntity.created(URI.create("/api/carritos/" + creado.getIdCarrito())).body(creado);
    }

    @PostMapping("/{idCarrito}/lineas")
    public ResponseEntity<CarritoResponse> anadirLinea(@PathVariable Long idCarrito, @RequestBody LineaRequest req) {
        return service.añadeLinea(idCarrito, req)
                .flatMap(c -> service.obtenerResponse(c.getIdCarrito()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idCarrito}")
    public ResponseEntity<Carrito> actualizar(@PathVariable Long idCarrito, @RequestBody CarritoRequest req) {
        return service.actualizar(idCarrito, req)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idCarrito}")
    public ResponseEntity<Void> borrar(@PathVariable Long idCarrito) {
        boolean ok = service.borrar(idCarrito);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idCarrito}/lineas/{idLinea}")
    public ResponseEntity<Void> borrarLinea(
            @PathVariable Long idCarrito,
            @PathVariable Long idLinea
    ) {
        boolean ok = service.borraLinea(idCarrito, idLinea);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
