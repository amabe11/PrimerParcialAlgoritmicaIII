package com.upb.libreria.model

/**
 * Clase base abstracta para las personas del sistema (autores, clientes y empleados).
 * (Abstracción + herencia)
 */
abstract class Persona(
    val id: Int,
    val nombre: String,
    val apellido: String
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(apellido.isNotBlank()) { "El apellido no puede estar vacío" }
    }

    fun nombreCompleto(): String = "$nombre $apellido"

    fun iniciales(): String = "${nombre.first()}${apellido.first()}".uppercase()

    /** Cada subclase define su rol dentro de la librería (polimorfismo). */
    abstract fun rol(): String

    override fun toString(): String = "${rol()}: ${nombreCompleto()}"
}
