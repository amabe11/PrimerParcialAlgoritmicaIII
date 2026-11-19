package com.upb.libreria.model

/** Autor de uno o varios libros. */
class Autor(
    id: Int,
    nombre: String,
    apellido: String,
    val nacionalidad: String,
    val anioNacimiento: Int,
    val biografia: String
) : Persona(id, nombre, apellido) {

    fun edad(anioActual: Int): Int = anioActual - anioNacimiento

    override fun rol(): String = "Autor"
}
