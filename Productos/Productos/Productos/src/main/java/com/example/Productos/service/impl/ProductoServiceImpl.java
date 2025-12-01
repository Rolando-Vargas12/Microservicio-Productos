package com.example.Productos.service.impl;

import com.example.Productos.entity.Producto;
import com.example.Productos.repository.ProductoRepository;
import com.example.Productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    // Constantes de validación
    private static final int CODIGO_MIN_LENGTH = 3;
    private static final int CODIGO_MAX_LENGTH = 20;
    private static final int NOMBRE_MIN_LENGTH = 3;
    private static final int NOMBRE_MAX_LENGTH = 100;
    private static final double PRECIO_MINIMO = 0.01;

    @Override
    public Producto crearProducto(Producto producto) {
        log.info("Iniciando creación de producto con código: {}", producto.getCodigo());

        // Validaciones
        validarProductoParaCreacion(producto);

        // Verificar que no exista un producto con el mismo código
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + producto.getCodigo());
        }

        // Establecer valores por defecto
        producto.setActivo(true);
        producto.setFechaCreacion(System.currentTimeMillis());
        producto.setFechaActualizacion(System.currentTimeMillis());

        Producto productoCreado = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", productoCreado.getId());

        return productoCreado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        log.info("Obteniendo todos los productos activos");
        return productoRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosProductos() {
        log.info("Obteniendo todos los productos (incluyendo inactivos)");
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Long id) {
        log.info("Buscando producto por ID: {}", id);
        validarId(id);
        return productoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorCodigo(String codigo) {
        log.info("Buscando producto por código: {}", codigo);
        validarCodigo(codigo);
        return productoRepository.findByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);
        validarNombre(nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

   @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        log.info("Iniciando actualización de producto con ID: {}", id);

        validarId(id);
        // validarProductoParaActualizacion(producto); // Opcional

        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        // ... validaciones de código ...

        // Actualizar campos
        productoExistente.setCodigo(producto.getCodigo());
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setCantidad(producto.getCantidad());    
        productoExistente.setImg(producto.getImg());
        productoExistente.setActivo(producto.getActivo());
        productoExistente.setFechaActualizacion(System.currentTimeMillis());

        Producto productoActualizado = productoRepository.save(productoExistente);
        log.info("Producto actualizado exitosamente con ID: {}", id);

        return productoActualizado;
    }

    @Override
    public void eliminarProducto(Long id) {
        log.info("Iniciando borrado lógico de producto con ID: {}", id);

        validarId(id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        producto.setActivo(false);
        producto.setFechaActualizacion(System.currentTimeMillis());
        productoRepository.save(producto);

        log.info("Producto desactivado exitosamente con ID: {}", id);
    }

    @Override
    public void eliminarProductoPermanentemente(Long id) {
        log.info("Iniciando borrado permanente de producto con ID: {}", id);

        validarId(id);

        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
        log.info("Producto eliminado permanentemente con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProductoPorCodigo(String codigo) {
        log.info("Verificando existencia de producto con código: {}", codigo);
        validarCodigo(codigo);
        return productoRepository.existsByCodigo(codigo);
    }

    // Métodos de validación internos

    /**
     * Valida un producto para su creación
     */
    private void validarProductoParaCreacion(Producto producto) {
        log.debug("Validando producto para creación");

        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        validarCodigo(producto.getCodigo());
        validarNombre(producto.getNombre());
        validarPrecio(producto.getPrecio());
        validarCantidad(producto.getCantidad());
    }

    /**
     * Valida un producto para su actualización
     */
    private void validarProductoParaActualizacion(Producto producto) {
        log.debug("Validando producto para actualización");

        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        validarCodigo(producto.getCodigo());
        validarNombre(producto.getNombre());
        validarPrecio(producto.getPrecio());
        validarCantidad(producto.getCantidad());
    }

    /**
     * Valida el código del producto
     */
    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío");
        }

        codigo = codigo.trim();

        if (codigo.length() < CODIGO_MIN_LENGTH || codigo.length() > CODIGO_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("El código debe tener entre %d y %d caracteres",
                            CODIGO_MIN_LENGTH, CODIGO_MAX_LENGTH));
        }

        if (!codigo.matches("^[a-zA-Z0-9-_]+$")) {
            throw new IllegalArgumentException("El código solo puede contener letras, números, guiones y guiones bajos");
        }
    }

    /**
     * Valida el nombre del producto
     */
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        nombre = nombre.trim();

        if (nombre.length() < NOMBRE_MIN_LENGTH || nombre.length() > NOMBRE_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("El nombre debe tener entre %d y %d caracteres",
                            NOMBRE_MIN_LENGTH, NOMBRE_MAX_LENGTH));
        }
    }

    /**
     * Valida el precio del producto
     */
    private void validarPrecio(Double precio) {
        if (precio == null) {
            throw new IllegalArgumentException("El precio del producto no puede ser nulo");
        }

        if (precio < PRECIO_MINIMO) {
            throw new IllegalArgumentException(
                    String.format("El precio debe ser mayor a %.2f", PRECIO_MINIMO));
        }

        if (precio > 999999.99) {
            throw new IllegalArgumentException("El precio no puede exceder 999999.99");
        }
    }

    /**
     * Valida la cantidad del producto
     */
    private void validarCantidad(Integer cantidad) {
        if (cantidad == null) {
            throw new IllegalArgumentException("La cantidad del producto no puede ser nula");
        }

        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        if (cantidad > 1000000) {
            throw new IllegalArgumentException("La cantidad no puede exceder 1,000,000");
        }
    }

    /**
     * Valida un ID
     */
    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
    }
}