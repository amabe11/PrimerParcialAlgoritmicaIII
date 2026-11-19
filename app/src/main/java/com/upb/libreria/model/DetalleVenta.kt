package com.upb.libreria.model

/** Línea de una venta: un producto con su cantidad y precio unitario. */
class DetalleVenta(
    val producto: Producto,
    val cantidad: Int
) {
    init {
        require(cantidad > 0) { "La cantidad debe ser positiva" }
    }

    /** Se congela el precio al momento de la venta. */
    val precioUnitario: Double = producto.precioVenta()

    fun subtotal(): Double = precioUnitario * cantidad
}
