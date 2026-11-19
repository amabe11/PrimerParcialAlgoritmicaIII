package com.upb.libreria.model

/** Empleado de la librería que atiende las ventas. */
class Empleado(
    id: Int,
    nombre: String,
    apellido: String,
    val cargo: String,
    salario: Double
) : Persona(id, nombre, apellido) {

    init {
        require(salario > 0) { "El salario debe ser positivo" }
    }

    /** Encapsulamiento: el salario solo se modifica con aumentarSalario(). */
    var salario: Double = salario
        private set

    fun aumentarSalario(porcentaje: Double) {
        require(porcentaje > 0) { "El porcentaje debe ser positivo" }
        salario += salario * porcentaje / 100
    }

    override fun rol(): String = "Empleado"
}
