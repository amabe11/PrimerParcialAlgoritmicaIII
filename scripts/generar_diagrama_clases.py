"""
Checkpoint 1 - Genera el diagrama de clases UML del caso de estudio "Librería".

Salida:
    docs/checkpoint1/diagrama_clases_libreria.drawio  (editable en https://app.diagrams.net)
    docs/checkpoint1/diagrama_clases_libreria.png

Ejecutar desde la raíz del repositorio:
    python scripts/generar_diagrama_clases.py
"""
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from drawio_lib import (Diagrama, AZUL, VERDE, NARANJA, MORADO, AMARILLO, GRIS,  # noqa: E402
                        HERENCIA, REALIZACION, ASOCIACION, AGREGACION, COMPOSICION)

RAIZ = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SALIDA = os.path.join(RAIZ, "docs", "checkpoint1")

d = Diagrama("Diagrama de clases - Libreria")

d.texto("<b style='font-size:22px'>Librería — Diagrama de clases (UML)</b><br>"
        "<span style='font-size:13px'>Programación III · Primer Parcial · Caso de estudio: Librería</span>",
        20, 5, 700, 50, estilo_extra="align=left;verticalAlign=top;", html_crudo=True)

# ---------------------------------------------------------------- Interfaces
vendible = d.clase_uml("Vendible", [], [
    "+ precioVenta(): Double",
    "+ descripcionCorta(): String",
], 1250, 70, w=280, color=GRIS, estereotipo="interface")

# ---------------------------------------------------------------- Personas
persona = d.clase_uml("Persona", [
    "+ id: Int",
    "+ nombre: String",
    "+ apellido: String",
], [
    "+ nombreCompleto(): String",
    "+ iniciales(): String",
    "+ rol(): String {abstract}",
    "+ toString(): String",
], 330, 70, w=280, color=AZUL, estereotipo="abstract", abstracta=True)

empleado = d.clase_uml("Empleado", [
    "+ cargo: String",
    "+ salario: Double {readOnly}",
], [
    "+ aumentarSalario(porcentaje: Double)",
    "+ rol(): String",
], 20, 340, w=290, color=AZUL)

cliente = d.clase_uml("Cliente", [
    "+ ci: String",
    "+ email: String",
    "+ telefono: String",
    "+ puntos: Int {readOnly}",
], [
    "+ acumularPuntos(monto: Double)",
    "+ rol(): String",
], 330, 340, w=280, color=AZUL)

autor = d.clase_uml("Autor", [
    "+ nacionalidad: String",
    "+ anioNacimiento: Int",
    "+ biografia: String",
], [
    "+ edad(anioActual: Int): Int",
    "+ rol(): String",
], 640, 340, w=280, color=AZUL)

# ---------------------------------------------------------------- Productos
producto = d.clase_uml("Producto", [
    "+ id: Int",
    "+ nombre: String",
    "+ precio: Double",
    "+ stock: Int {readOnly}",
], [
    "+ hayStock(): Boolean",
    "+ reducirStock(cantidad: Int): Boolean",
    "+ reponerStock(cantidad: Int)",
    "+ precioVenta(): Double",
], 1250, 250, w=300, color=VERDE, estereotipo="abstract", abstracta=True)

libro = d.clase_uml("Libro", [
    "+ isbn: String",
    "+ titulo: String",
    "+ autor: Autor",
    "+ editorial: Editorial",
    "+ genero: Genero",
    "+ paginas: Int",
    "+ anioPublicacion: Int",
], [
    "+ descripcionCorta(): String",
    "+ esNovedad(anioActual: Int): Boolean",
], 1000, 640, w=300, color=VERDE)

articulo = d.clase_uml("ArticuloPapeleria", [
    "+ marca: String",
    "+ categoria: CategoriaArticulo",
    "+ descuento: Double",
], [
    "+ precioVenta(): Double",
    "+ descripcionCorta(): String",
], 1360, 640, w=300, color=VERDE)

editorial = d.clase_uml("Editorial", [
    "+ id: Int",
    "+ nombre: String",
    "+ pais: String",
], [], 1000, 960, w=230, color=VERDE, estereotipo="data class")

genero = d.clase_uml("Genero", [
    "NOVELA", "CUENTO", "FANTASIA", "CIENCIA_FICCION",
    "POESIA", "HISTORIA", "INFANTIL", "ENSAYO",
], ["+ etiqueta: String"], 1260, 960, w=200, color=AMARILLO, estereotipo="enumeration")

categoria = d.clase_uml("CategoriaArticulo", [
    "CUADERNOS", "ESCRITURA", "ARTE", "OFICINA",
], ["+ etiqueta: String"], 1490, 960, w=200, color=AMARILLO, estereotipo="enumeration")

# ---------------------------------------------------------------- Ventas
venta = d.clase_uml("Venta", [
    "+ id: Int",
    "+ fecha: String",
    "+ cliente: Cliente",
    "+ empleado: Empleado",
    "- detalles: MutableList<DetalleVenta>",
], [
    "+ agregarProducto(producto: Producto,",
    "      cantidad: Int): Boolean",
    "+ total(): Double",
    "+ cantidadArticulos(): Int",
], 20, 640, w=330, color=NARANJA)

detalle = d.clase_uml("DetalleVenta", [
    "+ producto: Producto",
    "+ cantidad: Int",
    "+ precioUnitario: Double",
], [
    "+ subtotal(): Double",
], 20, 960, w=330, color=NARANJA)

# ---------------------------------------------------------------- Librería (raíz)
libreria = d.clase_uml("Libreria", [
    "+ nombre: String",
    "+ direccion: String",
    "- productos: MutableList<Producto>",
    "- autores: MutableList<Autor>",
    "- clientes: MutableList<Cliente>",
    "- empleados: MutableList<Empleado>",
    "- ventas: MutableList<Venta>",
], [
    "+ agregarProducto(producto: Producto)",
    "+ registrarAutor(autor: Autor)",
    "+ registrarCliente(cliente: Cliente)",
    "+ registrarEmpleado(empleado: Empleado)",
    "+ registrarVenta(venta: Venta): Boolean",
    "+ listarLibros(): List<Libro>",
    "+ listarArticulos(): List<ArticuloPapeleria>",
    "+ listarAutores(): List<Autor>",
    "+ listarClientes(): List<Cliente>",
    "+ listarEmpleados(): List<Empleado>",
    "+ listarVentas(): List<Venta>",
    "+ librosDeAutor(autor: Autor): List<Libro>",
    "+ buscarLibros(texto: String,",
    "      genero: Genero?): List<Libro>",
    "+ productosConPocoStock(limite: Int): List<Producto>",
    "+ totalVentas(): Double",
], 560, 640, w=400, color=MORADO)

# ---------------------------------------------------------------- Relaciones
# Herencia / realización
d.relacion(producto, vendible, REALIZACION)
d.relacion(libro, producto, HERENCIA, salida=(0.9, 0), entrada=(0.0667, 1))
d.relacion(articulo, producto, HERENCIA, salida=(0.5, 0), entrada=(0.7, 1))
d.relacion(empleado, persona, HERENCIA, salida=(0.5, 0), entrada=(0.25, 1))
d.relacion(cliente, persona, HERENCIA, salida=(0.5, 0), entrada=(0.5, 1))
d.relacion(autor, persona, HERENCIA, salida=(0.5, 0), entrada=(0.75, 1))

# Asociaciones del dominio
d.relacion(libro, autor, ASOCIACION, "escrito por", "*", "1", salida=(0, 0.12), entrada=(1, 0.5),
           puntos=[(980, 690), (980, 420)])
d.relacion(libro, editorial, ASOCIACION, "publicado por", "*", "1", salida=(0.3, 1), entrada=(0.5, 0))
d.relacion(libro, genero, ASOCIACION, "", "*", "1", salida=(0.8, 1), entrada=(0.3, 0))
d.relacion(articulo, categoria, ASOCIACION, "", "*", "1", salida=(0.75, 1), entrada=(0.6, 0))
d.relacion(venta, cliente, ASOCIACION, "compra", "*", "1", salida=(0.85, 0), entrada=(0.3, 1))
d.relacion(venta, empleado, ASOCIACION, "atiende", "*", "1", salida=(0.3, 0), entrada=(0.5, 1))
d.relacion(venta, detalle, COMPOSICION, "", "1", "1..*", salida=(0.5, 1), entrada=(0.5, 0))
d.relacion(detalle, producto, ASOCIACION, "vende", "*", "1", salida=(1, 0.5), entrada=(1, 0.5),
           puntos=[(380, 1028), (380, 1240), (1730, 1240), (1730, 396)])

# Agregaciones de la Librería
d.relacion(libreria, venta, AGREGACION, "", "1", "*", salida=(0, 0.12), entrada=(1, 0.12))
d.relacion(libreria, autor, AGREGACION, "", "1", "*", salida=(0.6, 0), entrada=(0.5, 1))
d.relacion(libreria, cliente, AGREGACION, "", "1", "*", salida=(0.2, 0), entrada=(0.75, 1),
           puntos=[(640, 580), (540, 580)])
d.relacion(libreria, empleado, AGREGACION, "", "1", "*", salida=(0.08, 0), entrada=(0.75, 1),
           puntos=[(592, 610), (237, 610)])
d.relacion(libreria, producto, AGREGACION, "", "1", "*", salida=(0.85, 0), entrada=(0, 0.75),
           puntos=[(900, 600), (1180, 600), (1180, 427)])

if __name__ == "__main__":
    ruta = os.path.join(SALIDA, "diagrama_clases_libreria.drawio")
    d.guardar(ruta)
    d.max_y = max(d.max_y, 1260)
    d.max_x = max(d.max_x, 1750)
    d.exportar_png(ruta, os.path.join(SALIDA, "diagrama_clases_libreria.png"))
