package com.blanca.practica.practica2.service;

import com.blanca.practica.practica2.model.Carrito;
import com.blanca.practica.practica2.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoRepository repo;

    public CarritoService(CarritoRepository repo) {
        this.repo = repo;
    }

    public List<Carrito> listar() {
        return repo.findAll();
    }

    public Optional<Carrito> obtener(Long idCarrito) {
        return repo.findById(idCarrito);
    }

    public Carrito crear(Carrito carrito) {
        validar(carrito);

        carrito.setPrecioFinal(calcularPrecioFinal(
                carrito.getUnidades(),
                carrito.getPrecioProducto()
        ));

        return repo.save(carrito);
    }

    public Optional<Carrito> actualizar(Long idCarrito, Carrito carrito) {
        validar(carrito);

        return repo.findById(idCarrito).map(actual -> {
            actual.setIdArticulo(carrito.getIdArticulo());
            actual.setDescripcion(carrito.getDescripcion());
            actual.setUnidades(carrito.getUnidades());
            actual.setPrecioProducto(carrito.getPrecioProducto());
            actual.setPrecioFinal(calcularPrecioFinal(
                    carrito.getUnidades(),
                    carrito.getPrecioProducto()
            ));
            return repo.save(actual);
        });
    }

    public boolean borrar(Long idCarrito) {
        if (!repo.existsById(idCarrito)) {
            return false;
        }
        repo.deleteById(idCarrito);
        return true;
    }

    private Double calcularPrecioFinal(Integer unidades, Double precioProducto) {
        return unidades * precioProducto;
    }

    private void validar(Carrito carrito) {
        if (carrito.getIdArticulo() == null)
            throw new IllegalArgumentException("idArticulo es obligatorio");
        if (carrito.getDescripcion() == null || carrito.getDescripcion().isBlank())
            throw new IllegalArgumentException("descripcion es obligatoria");
        if (carrito.getUnidades() == null || carrito.getUnidades() <= 0)
            throw new IllegalArgumentException("unidades debe ser > 0");
        if (carrito.getPrecioProducto() == null || carrito.getPrecioProducto() <= 0)
            throw new IllegalArgumentException("precioProducto debe ser > 0");
    }
}