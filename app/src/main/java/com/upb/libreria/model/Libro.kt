package com.upb.libreria.model

/** Libro del catálogo. Hereda de [Producto]; su nombre es el título. */
class Libro(
    id: Int,
    titulo: String,
    precio: Double,
    stock: Int,
    val isbn: String,
    val autor: Autor,
    val editorial: Editorial,
    val genero: Genero,
    val paginas: Int,
    val anioPublicacion: Int
) : Producto(id, titulo, precio, stock) {

    val titulo: String
        get() = nombre

    override fun descripcionCorta(): String = "$titulo — ${autor.nombreCompleto()}"

    /** Un libro es novedad si se publicó en los últimos 2 años. */
    fun esNovedad(anioActual: Int): Boolean = anioActual - anioPublicacion <= 2
}
