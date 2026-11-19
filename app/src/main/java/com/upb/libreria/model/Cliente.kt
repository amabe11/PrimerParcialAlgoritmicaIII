package com.upb.libreria.model

/** Cliente registrado que acumula puntos por sus compras. */
class Cliente(
    id: Int,
    nombre: String,
    apellido: String,
    val ci: String,
    val email: String,
    val telefono: String
) : Persona(id, nombre, apellido) {

    /** Encapsulamiento: los puntos solo cambian mediante acumularPuntos(). */
    var puntos: Int = 0
        private set

    /** Suma 1 punto por cada 10 Bs de compra. */
    fun acumularPuntos(monto: Double) {
        require(monto >= 0) { "El monto no puede ser negativo" }
        puntos += (monto / 10).toInt()
    }

    override fun rol(): String = "Cliente"
}
