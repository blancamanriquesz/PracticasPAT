package com.blanca.practica.practica2.service;

import com.blanca.practica.practica2.dto.CarritoRequest;
import com.blanca.practica.practica2.model.Carrito;
import com.blanca.practica.practica2.dto.CarritoResponse;
import com.blanca.practica.practica2.dto.LineaResponse;
import java.util.stream.Collectors;
import com.blanca.practica.practica2.repository.CarritoRepository;
import com.blanca.practica.practica2.dto.LineaRequest;
import com.blanca.practica.practica2.model.LineaCarrito;
import com.blanca.practica.practica2.repository.LineaCarritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CarritoService {

    private final CarritoRepository repo;
    private final LineaCarritoRepository lineaRepo;


    public CarritoService(CarritoRepository repo, LineaCarritoRepository lineaRepo) {
        this.repo = repo;
        this.lineaRepo = lineaRepo; }

    public List<Carrito> listar() {
        return repo.findAll();
    }

    public Optional<Carrito> obtener(Long idCarrito) {
        return repo.findById(idCarrito);
    }

    public Carrito crear(CarritoRequest req) {
        validar(req);

        Carrito c = new Carrito();
        c.setIdUsuario(req.getIdUsuario());
        c.setCorreoUsuario(req.getCorreoUsuario());
        c.setPrecioFinal(0.0);

        return repo.save(c);
    }

    public Optional<Carrito> actualizar(Long idCarrito, CarritoRequest req) {
        validar(req);

        return repo.findById(idCarrito).map(actual -> {
            actual.setIdUsuario(req.getIdUsuario());
            actual.setCorreoUsuario(req.getCorreoUsuario());
            return repo.save(actual);
        });
    }
    public Optional<Carrito> añadeLinea(Long idCarrito, LineaRequest req) {
        validarLinea(req);

        return repo.findById(idCarrito).map(carrito -> {
            LineaCarrito linea = new LineaCarrito();
            linea.setCarrito(carrito);
            linea.setIdArticulo(req.getIdArticulo());
            linea.setPrecioUnitario(req.getPrecioUnitario());
            linea.setUnidades(req.getUnidades());
            linea.setCosteLinea(req.getPrecioUnitario() * req.getUnidades());


            lineaRepo.save(linea);

            recalcularTotal(carrito);
            return repo.save(carrito);
        });
    }

    public boolean borraLinea(Long idCarrito, Long idLinea) {
        Optional<Carrito> carritoOpt = repo.findById(idCarrito);
        if (carritoOpt.isEmpty()) return false;

        Carrito carrito = carritoOpt.get();

        Optional<LineaCarrito> lineaOpt = lineaRepo.findById(idLinea);
        if (lineaOpt.isEmpty()) return false;

        LineaCarrito linea = lineaOpt.get();

        if (!linea.getCarrito().getIdCarrito().equals(idCarrito)) return false;

        lineaRepo.delete(linea);

        recalcularTotal(carrito);
        repo.save(carrito);
        return true;
    }

    public boolean borrar(Long idCarrito) {
        if (!repo.existsById(idCarrito)) return false;
        repo.deleteById(idCarrito);
        return true;
    }


    private void validar(CarritoRequest req) {
        if (req.getIdUsuario() == null) throw new IllegalArgumentException("idUsuario es obligatorio");
        if (req.getCorreoUsuario() == null || req.getCorreoUsuario().isBlank())
            throw new IllegalArgumentException("correo de usuario es obligatorio");

    }

    private void validarLinea(LineaRequest req) {
        if (req.getIdArticulo() == null) throw new IllegalArgumentException("idArticulo es obligatorio");
        if (req.getPrecioUnitario() == null || req.getPrecioUnitario() <= 0)
            throw new IllegalArgumentException("precioUnitario debe ser > 0");
        if (req.getUnidades() == null || req.getUnidades() <= 0)
            throw new IllegalArgumentException("unidades debe ser > 0");
    }

    private void recalcularTotal(Carrito carrito) {
        double total = lineaRepo.findByCarrito_IdCarrito(carrito.getIdCarrito())
                .stream()
                .mapToDouble(l -> l.getCosteLinea() == null ? 0.0 : l.getCosteLinea())
                .sum();

        carrito.setPrecioFinal(total);
    }
    public Optional<CarritoResponse> obtenerResponse(Long idCarrito) {
        return repo.findById(idCarrito).map(this::toResponse);
    }

    private CarritoResponse toResponse(Carrito carrito) {
        var lineas = lineaRepo.findByCarrito_IdCarrito(carrito.getIdCarrito())
                .stream()
                .map(l -> new LineaResponse(
                        l.getIdLinea(),
                        l.getCarrito().getIdCarrito(),
                        l.getIdArticulo(),
                        l.getPrecioUnitario(),
                        l.getUnidades(),
                        l.getCosteLinea()
                ))
                .collect(Collectors.toList());

        return new CarritoResponse(
                carrito.getIdCarrito(),
                carrito.getIdUsuario(),
                carrito.getCorreoUsuario(),
                carrito.getPrecioFinal(),
                lineas
        );
    }
}
