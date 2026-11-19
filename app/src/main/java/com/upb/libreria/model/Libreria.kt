package com.upb.libreria.model

/**
 * Clase principal del dominio: agrupa productos, personas y ventas
 * y ofrece las operaciones de consulta que usa la interfaz.
 */
class Libreria(
    val nombre: String,
    val direccion: String
) {
    private val productos = mutableListOf<Producto>()
    private val autores = mutableListOf<Autor>()
    private val clientes = mutableListOf<Cliente>()
    private val empleados = mutableListOf<Empleado>()
    private val ventas = mutableListOf<Venta>()

    fun agregarProducto(producto: Producto) {
        require(productos.none { it.id == producto.id }) { "Ya existe un producto con id ${producto.id}" }
        productos.add(producto)
    }

    fun registrarAutor(autor: Autor) {
        if (autores.none { it.id == autor.id }) autores.add(autor)
    }

    fun registrarCliente(cliente: Cliente) {
        if (clientes.none { it.id == cliente.id }) clientes.add(cliente)
    }

    fun registrarEmpleado(empleado: Empleado) {
        if (empleados.none { it.id == empleado.id }) empleados.add(empleado)
    }

    /** Registra una venta no vacía y otorga puntos al cliente. */
    fun registrarVenta(venta: Venta): Boolean {
        if (venta.detalles.isEmpty()) return false
        ventas.add(venta)
        venta.cliente.acumularPuntos(venta.total())
        return true
    }

    fun listarLibros(): List<Libro> = productos.filterIsInstance<Libro>()

    fun listarArticulos(): List<ArticuloPapeleria> = productos.filterIsInstance<ArticuloPapeleria>()

    fun listarAutores(): List<Autor> = autores.sortedBy { it.apellido }

    fun listarClientes(): List<Cliente> = clientes.toList()

    fun listarEmpleados(): List<Empleado> = empleados.toList()

    fun listarVentas(): List<Venta> = ventas.toList()

    fun librosDeAutor(autor: Autor): List<Libro> = listarLibros().filter { it.autor.id == autor.id }

    /** Busca por título, autor o ISBN y, opcionalmente, filtra por género. */
    fun buscarLibros(texto: String, genero: Genero? = null): List<Libro> {
        val consulta = texto.trim().lowercase()
        return listarLibros().filter { libro ->
            val coincideTexto = consulta.isEmpty() ||
                libro.titulo.lowercase().contains(consulta) ||
                libro.autor.nombreCompleto().lowercase().contains(consulta) ||
                libro.isbn.contains(consulta)
            coincideTexto && (genero == null || libro.genero == genero)
        }
    }

    fun productosConPocoStock(limite: Int = 5): List<Producto> = productos.filter { it.stock <= limite }

    fun totalVentas(): Double = ventas.sumOf { it.total() }
}
