package com.upb.libreria

import com.upb.libreria.data.LibreriaRepositorio
import com.upb.libreria.model.ArticuloPapeleria
import com.upb.libreria.model.Autor
import com.upb.libreria.model.CategoriaArticulo
import com.upb.libreria.model.Cliente
import com.upb.libreria.model.Editorial
import com.upb.libreria.model.Empleado
import com.upb.libreria.model.Genero
import com.upb.libreria.model.Libro
import com.upb.libreria.model.Persona
import com.upb.libreria.model.Producto
import com.upb.libreria.model.Venta
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LibreriaTest {

    private val autor = Autor(1, "Jorge Luis", "Borges", "Argentina", 1899, "")
    private val editorial = Editorial(1, "Alfaguara", "España")
    private fun libro(stock: Int = 5) =
        Libro(1, "Ficciones", 100.0, stock, "123", autor, editorial, Genero.CUENTO, 224, 1944)

    @Test
    fun polimorfismo_rolDePersonas() {
        val personas: List<Persona> = listOf(
            autor,
            Cliente(1, "Ana", "Quispe", "1", "a@a.com", "1"),
            Empleado(1, "Carla", "Rojas", "Vendedora", 3000.0)
        )
        assertEquals(listOf("Autor", "Cliente", "Empleado"), personas.map { it.rol() })
        assertEquals("JB", autor.iniciales())
    }

    @Test
    fun polimorfismo_precioVentaAplicaDescuentoSoloEnPapeleria() {
        val productos: List<Producto> = listOf(
            libro(),
            ArticuloPapeleria(2, "Bolígrafo", 10.0, 5, "BIC", CategoriaArticulo.ESCRITURA, descuento = 0.2)
        )
        assertEquals(100.0, productos[0].precioVenta(), 0.001)
        assertEquals(8.0, productos[1].precioVenta(), 0.001)
    }

    @Test
    fun venta_descuentaStockYCalculaTotal() {
        val l = libro(stock = 3)
        val venta = Venta(1, "2026-11-19", Cliente(1, "Ana", "Q", "1", "a", "1"),
            Empleado(1, "C", "R", "V", 1000.0))
        assertTrue(venta.agregarProducto(l, 2))
        assertFalse(venta.agregarProducto(l, 2)) // solo queda 1
        assertEquals(1, l.stock)
        assertEquals(200.0, venta.total(), 0.001)
        assertEquals(2, venta.cantidadArticulos())
    }

    @Test
    fun repositorio_cargaDatosYBuscaLibros() {
        val libreria = LibreriaRepositorio.crearLibreria()
        assertEquals(14, libreria.listarLibros().size)
        assertEquals(5, libreria.listarArticulos().size)
        assertEquals(7, libreria.listarAutores().size)
        assertEquals(2, libreria.listarVentas().size)

        val tolkien = libreria.listarAutores().first { it.apellido == "Tolkien" }
        assertEquals(2, libreria.librosDeAutor(tolkien).size)
        assertEquals(2, libreria.buscarLibros("borges").size)
        assertEquals(2, libreria.buscarLibros("", Genero.FANTASIA).size)
        assertTrue(libreria.totalVentas() > 0)
    }
}
