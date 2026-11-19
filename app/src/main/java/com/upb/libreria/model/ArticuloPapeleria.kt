package com.upb.libreria.model

/** Artículo de papelería (cuadernos, bolígrafos, etc.). */
class ArticuloPapeleria(
    id: Int,
    nombre: String,
    precio: Double,
    stock: Int,
    val marca: String,
    val categoria: CategoriaArticulo,
    val descuento: Double = 0.0
) : Producto(id, nombre, precio, stock) {

    init {
        require(descuento in 0.0..1.0) { "El descuento debe estar entre 0 y 1" }
    }

    /** Polimorfismo: los artículos de papelería aplican su descuento. */
    override fun precioVenta(): Double = precio * (1 - descuento)

    override fun descripcionCorta(): String = "$nombre ($marca)"
}
