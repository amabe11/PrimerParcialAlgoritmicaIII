package com.upb.libreria.model

/** Venta realizada por un empleado a un cliente. Se compone de varios [DetalleVenta]. */
class Venta(
    val id: Int,
    val fecha: String,
    val cliente: Cliente,
    val empleado: Empleado
) {
    private val _detalles = mutableListOf<DetalleVenta>()

    /** Vista de solo lectura de los detalles (encapsulamiento). */
    val detalles: List<DetalleVenta>
        get() = _detalles.toList()

    /** Agrega un producto si hay stock suficiente y lo descuenta del inventario. */
    fun agregarProducto(producto: Producto, cantidad: Int): Boolean {
        if (!producto.reducirStock(cantidad)) return false
        _detalles.add(DetalleVenta(producto, cantidad))
        return true
    }

    fun total(): Double = _detalles.sumOf { it.subtotal() }

    fun cantidadArticulos(): Int = _detalles.sumOf { it.cantidad }
}
