package com.upb.libreria.data

import com.upb.libreria.model.ArticuloPapeleria
import com.upb.libreria.model.Autor
import com.upb.libreria.model.CategoriaArticulo
import com.upb.libreria.model.Cliente
import com.upb.libreria.model.Editorial
import com.upb.libreria.model.Empleado
import com.upb.libreria.model.Genero
import com.upb.libreria.model.Libreria
import com.upb.libreria.model.Libro
import com.upb.libreria.model.Venta

/**
 * Fuente de datos en memoria: construye una [Libreria] de ejemplo
 * con autores, libros, artículos, clientes, empleados y ventas.
 */
object LibreriaRepositorio {

    const val ANIO_ACTUAL = 2026

    val libreria: Libreria by lazy { crearLibreria() }

    fun crearLibreria(): Libreria {
        val libreria = Libreria("Librería Gutenberg", "Av. Arce #2345, La Paz")

        // ---------------- Editoriales
        val sudamericana = Editorial(1, "Editorial Sudamericana", "Argentina")
        val planeta = Editorial(2, "Planeta", "España")
        val minotauro = Editorial(3, "Minotauro", "España")
        val alfaguara = Editorial(4, "Alfaguara", "España")
        val salamandra = Editorial(5, "Salamandra", "España")
        val plural = Editorial(6, "Plural Editores", "Bolivia")

        // ---------------- Autores
        val garciaMarquez = Autor(
            1, "Gabriel", "García Márquez", "Colombia", 1927,
            "Premio Nobel de Literatura 1982, figura central del realismo mágico."
        )
        val allende = Autor(
            2, "Isabel", "Allende", "Chile", 1942,
            "Una de las novelistas en español más leídas del mundo."
        )
        val tolkien = Autor(
            3, "J. R. R.", "Tolkien", "Reino Unido", 1892,
            "Filólogo y escritor, creador de la Tierra Media."
        )
        val orwell = Autor(
            4, "George", "Orwell", "Reino Unido", 1903,
            "Novelista y ensayista conocido por sus distopías políticas."
        )
        val borges = Autor(
            5, "Jorge Luis", "Borges", "Argentina", 1899,
            "Maestro del cuento corto y de la literatura fantástica."
        )
        val pazSoldan = Autor(
            6, "Edmundo", "Paz Soldán", "Bolivia", 1967,
            "Narrador boliviano, referente de la nueva literatura latinoamericana."
        )
        val rowling = Autor(
            7, "J. K.", "Rowling", "Reino Unido", 1965,
            "Autora de la saga de Harry Potter."
        )
        listOf(garciaMarquez, allende, tolkien, orwell, borges, pazSoldan, rowling)
            .forEach(libreria::registrarAutor)

        // ---------------- Libros
        listOf(
            Libro(1, "Cien años de soledad", 150.0, 12, "9780307474728", garciaMarquez, sudamericana, Genero.NOVELA, 471, 1967),
            Libro(2, "El amor en los tiempos del cólera", 135.0, 4, "9780307387264", garciaMarquez, sudamericana, Genero.NOVELA, 464, 1985),
            Libro(3, "Crónica de una muerte anunciada", 90.0, 9, "9780307475961", garciaMarquez, sudamericana, Genero.NOVELA, 144, 1981),
            Libro(4, "La casa de los espíritus", 140.0, 7, "9788401352836", allende, planeta, Genero.NOVELA, 496, 1982),
            Libro(5, "El viento conoce mi nombre", 165.0, 15, "9788401032004", allende, planeta, Genero.NOVELA, 336, 2025),
            Libro(6, "El Hobbit", 120.0, 20, "9788445000663", tolkien, minotauro, Genero.FANTASIA, 310, 1937),
            Libro(7, "El Señor de los Anillos", 280.0, 3, "9788445000670", tolkien, minotauro, Genero.FANTASIA, 1392, 1954),
            Libro(8, "1984", 110.0, 18, "9788499890944", orwell, planeta, Genero.CIENCIA_FICCION, 352, 1949),
            Libro(9, "Rebelión en la granja", 85.0, 0, "9788499890951", orwell, planeta, Genero.ENSAYO, 144, 1945),
            Libro(10, "Ficciones", 105.0, 6, "9788499089508", borges, alfaguara, Genero.CUENTO, 224, 1944),
            Libro(11, "El Aleph", 100.0, 8, "9788499089515", borges, alfaguara, Genero.CUENTO, 208, 1949),
            Libro(12, "Río Fugitivo", 95.0, 10, "9789995408214", pazSoldan, plural, Genero.NOVELA, 312, 1998),
            Libro(13, "Los vivos y los muertos", 115.0, 5, "9788420407098", pazSoldan, alfaguara, Genero.NOVELA, 208, 2009),
            Libro(14, "Harry Potter y la piedra filosofal", 130.0, 25, "9788478884452", rowling, salamandra, Genero.INFANTIL, 256, 1997)
        ).forEach(libreria::agregarProducto)

        // ---------------- Artículos de papelería
        listOf(
            ArticuloPapeleria(101, "Cuaderno universitario 100 hojas", 25.0, 40, "Norma", CategoriaArticulo.CUADERNOS),
            ArticuloPapeleria(102, "Bolígrafo azul (caja x12)", 30.0, 25, "BIC", CategoriaArticulo.ESCRITURA, descuento = 0.10),
            ArticuloPapeleria(103, "Lápices de colores x24", 55.0, 3, "Faber-Castell", CategoriaArticulo.ARTE),
            ArticuloPapeleria(104, "Resaltadores x6", 40.0, 12, "Stabilo", CategoriaArticulo.ESCRITURA, descuento = 0.15),
            ArticuloPapeleria(105, "Archivador palanca", 35.0, 8, "Pagoda", CategoriaArticulo.OFICINA)
        ).forEach(libreria::agregarProducto)

        // ---------------- Personas
        val ana = Cliente(1, "Ana", "Quispe", "6012345", "ana.quispe@mail.com", "70011223")
        val luis = Cliente(2, "Luis", "Mamani", "4876543", "luis.mamani@mail.com", "71234567")
        listOf(ana, luis).forEach(libreria::registrarCliente)

        val carla = Empleado(1, "Carla", "Rojas", "Vendedora", 3500.0)
        val marco = Empleado(2, "Marco", "Vargas", "Administrador", 5200.0)
        listOf(carla, marco).forEach(libreria::registrarEmpleado)

        // ---------------- Ventas de ejemplo
        val libros = libreria.listarLibros().associateBy { it.id }
        val articulos = libreria.listarArticulos().associateBy { it.id }

        val venta1 = Venta(1, "2026-11-10", ana, carla).apply {
            agregarProducto(libros.getValue(1), 1)
            agregarProducto(articulos.getValue(101), 2)
        }
        val venta2 = Venta(2, "2026-11-12", luis, carla).apply {
            agregarProducto(libros.getValue(6), 1)
            agregarProducto(libros.getValue(8), 1)
        }
        libreria.registrarVenta(venta1)
        libreria.registrarVenta(venta2)

        return libreria
    }
}
