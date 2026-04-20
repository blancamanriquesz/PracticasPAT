package com.blanca.practica.practica2.service;

import com.blanca.practica.practica2.dto.CarritoRequest;
import com.blanca.practica.practica2.model.Carrito;
import com.blanca.practica.practica2.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarritoService {

    private final CarritoRepository repo;
    private final AtomicLong seq = new AtomicLong(1);

    public CarritoService(CarritoRepository repo) {
        this.repo = repo;
    }

    public List<Carrito> listar() {
        return repo.findAll();
    }

    public Optional<Carrito> obtener(Long idCarrito) {
        return repo.findById(idCarrito);
    }

    public Carrito crear(CarritoRequest req) {
        validar(req);

        Long id = seq.getAndIncrement();
        Carrito c = new Carrito();
        c.setIdCarrito(id);
        c.setIdArticulo(req.getIdArticulo());
        c.setDescripcion(req.getDescripcion());
        c.setUnidades(req.getUnidades());
        c.setPrecioProducto(req.getPrecioProducto());
        c.setPrecioFinal(calcularPrecioFinal(req.getUnidades(), req.getPrecioProducto()));

        return repo.save(c);
    }

    public Optional<Carrito> actualizar(Long idCarrito, CarritoRequest req) {
        validar(req);

        return repo.findById(idCarrito).map(actual -> {
            actual.setIdArticulo(req.getIdArticulo());
            actual.setDescripcion(req.getDescripcion());
            actual.setUnidades(req.getUnidades());
            actual.setPrecioProducto(req.getPrecioProducto());
            actual.setPrecioFinal(calcularPrecioFinal(req.getUnidades(), req.getPrecioProducto()));
            return repo.save(actual);
        });
    }

    public boolean borrar(Long idCarrito) {
        if (!repo.existsById(idCarrito)) return false;
        repo.deleteById(idCarrito);
        return true;
    }

    private Double calcularPrecioFinal(Integer unidades, Double precioProducto) {
        return unidades * precioProducto;
    }

    private void validar(CarritoRequest req) {
        if (req.getIdArticulo() == null)
            throw new IllegalArgumentException("idArticulo es obligatorio");
        if (req.getDescripcion() == null || req.getDescripcion().isBlank())
            throw new IllegalArgumentException("descripcion es obligatoria");
        if (req.getUnidades() == null || req.getUnidades() <= 0)
            throw new IllegalArgumentException("unidades debe ser > 0");
        if (req.getPrecioProducto() == null || req.getPrecioProducto() <= 0)
            throw new IllegalArgumentException("precioProducto debe ser > 0");
    }
}