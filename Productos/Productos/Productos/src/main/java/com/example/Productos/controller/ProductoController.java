package com.example.Productos.controller;

import com.example.Productos.entity.Producto;
import com.example.Productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Crear un nuevo producto (POST)
     */
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            log.info("Recibida solicitud POST para crear producto");
            Producto productoCreado = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(crearRespuestaExito("Producto creado exitosamente", productoCreado));
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Obtener todos los productos (GET)
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosProductos() {
        try {
            log.info("Recibida solicitud GET para obtener todos los productos");
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(crearRespuestaExito("Productos obtenidos exitosamente", productos));
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Obtener todos los productos (incluyendo inactivos)
     */
    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodosProductos() {
        try {
            log.info("Recibida solicitud GET para obtener todos los productos");
            List<Producto> productos = productoService.obtenerTodosProductos();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(crearRespuestaExito("Productos obtenidos exitosamente", productos));
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Obtener un producto por ID (GET)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        try {
            log.info("Recibida solicitud GET para obtener producto con ID: {}", id);
            Optional<Producto> producto = productoService.obtenerProductoPorId(id);
            if (producto.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(crearRespuestaExito("Producto obtenido exitosamente", producto.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(crearRespuestaError("No encontrado", "Producto no encontrado con ID: " + id));
            }
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Obtener producto por código (GET)
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> obtenerProductoPorCodigo(@PathVariable String codigo) {
        try {
            log.info("Recibida solicitud GET para obtener producto con código: {}", codigo);
            Optional<Producto> producto = productoService.obtenerProductoPorCodigo(codigo);
            if (producto.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(crearRespuestaExito("Producto obtenido exitosamente", producto.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(crearRespuestaError("No encontrado", "Producto no encontrado con código: " + codigo));
            }
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Buscar productos por nombre (GET)
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            log.info("Recibida solicitud GET para buscar productos por nombre: {}", nombre);
            List<Producto> productos = productoService.buscarPorNombre(nombre);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(crearRespuestaExito("Búsqueda realizada exitosamente", productos));
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error en búsqueda: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Actualizar un producto (PUT)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            log.info("Recibida solicitud PUT para actualizar producto con ID: {}", id);
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(crearRespuestaExito("Producto actualizado exitosamente", productoActualizado));
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Eliminar un producto (DELETE) - Borrado lógico
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            log.info("Recibida solicitud DELETE para eliminar producto con ID: {}", id);
            productoService.eliminarProducto(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(crearRespuestaExito("Producto eliminado exitosamente", null));
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Eliminar permanentemente un producto (DELETE)
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<?> eliminarProductoPermanentemente(@PathVariable Long id) {
        try {
            log.info("Recibida solicitud DELETE para eliminar permanentemente producto con ID: {}", id);
            productoService.eliminarProductoPermanentemente(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(crearRespuestaExito("Producto eliminado permanentemente", null));
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Verificar si existe un producto por código
     */
    @GetMapping("/existe/{codigo}")
    public ResponseEntity<?> existeProductoPorCodigo(@PathVariable String codigo) {
        try {
            log.info("Verificando existencia de producto con código: {}", codigo);
            boolean existe = productoService.existeProductoPorCodigo(codigo);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("existe", existe);
            respuesta.put("codigo", codigo);
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } catch (IllegalArgumentException e) {
            log.error("Error en validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al verificar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Método auxiliar para crear respuesta de éxito
     */
    private Map<String, Object> crearRespuestaExito(String mensaje, Object data) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        respuesta.put("data", data);
        respuesta.put("estado", "exito");
        return respuesta;
    }

    /**
     * Método auxiliar para crear respuesta de error
     */
    private Map<String, Object> crearRespuestaError(String titulo, String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("titulo", titulo);
        respuesta.put("mensaje", mensaje);
        respuesta.put("estado", "error");
        return respuesta;
    }
}
