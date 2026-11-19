package com.upb.libreria.model

/**
 * Contrato para todo lo que se puede vender en la librería.
 * (Abstracción mediante interfaz)
 */
interface Vendible {
    /** Precio final de venta (puede incluir descuentos). */
    fun precioVenta(): Double

    /** Texto breve para mostrar el producto en listas y comprobantes. */
    fun descripcionCorta(): String
}
