package com.example.Productos.service;

import com.example.Productos.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    /**
     * Crear un nuevo producto
     * @param producto Objeto producto a crear
     * @return Producto creado
     */
    Producto crearProducto(Producto producto);

    /**
     * Obtener todos los productos activos
     * @return Lista de productos activos
     */
    List<Producto> obtenerTodosLosProductos();

    /**
     * Obtener un producto por ID
     * @param id ID del producto
     * @return Optional del producto
     */
    Optional<Producto> obtenerProductoPorId(Long id);

    /**
     * Obtener producto por código
     * @param codigo Código del producto
     * @return Optional del producto
     */
    Optional<Producto> obtenerProductoPorCodigo(String codigo);

    /**
     * Buscar productos por nombre
     * @param nombre Nombre del producto
     * @return Lista de productos que coincidan
     */
    List<Producto> buscarPorNombre(String nombre);

    /**
     * Actualizar un producto
     * @param id ID del producto a actualizar
     * @param producto Datos actualizados
     * @return Producto actualizado
     */
    Producto actualizarProducto(Long id, Producto producto);

    /**
     * Eliminar un producto (borrado lógico)
     * @param id ID del producto a eliminar
     */
    void eliminarProducto(Long id);

    /**
     * Eliminar un producto permanentemente
     * @param id ID del producto a eliminar
     */
    void eliminarProductoPermanentemente(Long id);

    /**
     * Verificar si existe un producto por código
     * @param codigo Código del producto
     * @return true si existe, false en caso contrario
     */
    boolean existeProductoPorCodigo(String codigo);

    /**
     * Obtener todos los productos (incluyendo inactivos)
     * @return Lista de todos los productos
     */
    List<Producto> obtenerTodosProductos();
}
