package com.upb.libreria.model

/**
 * Producto genérico del inventario. No se instancia directamente:
 * se especializa en [Libro] y [ArticuloPapeleria].
 */
abstract class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    stockInicial: Int
) : Vendible {

    init {
        require(precio >= 0) { "El precio no puede ser negativo" }
        require(stockInicial >= 0) { "El stock no puede ser negativo" }
    }

    /** Encapsulamiento: el stock solo cambia con reducirStock() / reponerStock(). */
    var stock: Int = stockInicial
        private set

    fun hayStock(): Boolean = stock > 0

    /** Descuenta unidades del inventario. Devuelve false si no hay suficientes. */
    fun reducirStock(cantidad: Int): Boolean {
        if (cantidad <= 0 || cantidad > stock) return false
        stock -= cantidad
        return true
    }

    fun reponerStock(cantidad: Int) {
        require(cantidad > 0) { "La cantidad debe ser positiva" }
        stock += cantidad
    }

    /** Por defecto el precio de venta es el precio de lista. */
    override fun precioVenta(): Double = precio
}
