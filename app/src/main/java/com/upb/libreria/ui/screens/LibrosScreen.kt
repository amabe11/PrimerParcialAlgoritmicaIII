package com.upb.libreria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.libreria.data.LibreriaRepositorio
import com.upb.libreria.model.Genero
import com.upb.libreria.model.Libreria
import com.upb.libreria.model.Libro
import com.upb.libreria.ui.theme.LibreriaTheme
import com.upb.libreria.ui.theme.NaranjaStock
import com.upb.libreria.ui.theme.RojoStock
import com.upb.libreria.ui.theme.VerdeStock
import java.util.Locale

/** Pantalla 1: catálogo de objetos [Libro] con búsqueda y filtro por [Genero]. */
@Composable
fun LibrosScreen(libreria: Libreria, modifier: Modifier = Modifier) {
    var consulta by rememberSaveable { mutableStateOf("") }
    var genero by rememberSaveable { mutableStateOf<Genero?>(null) }
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    val teclado = LocalSoftwareKeyboardController.current
    val foco = LocalFocusManager.current

    val generosDisponibles = remember(libreria) {
        val usados = libreria.listarLibros().map { it.genero }.toSet()
        Genero.entries.filter { it in usados }
    }
    val libros = remember(libreria, consulta, genero) { libreria.buscarLibros(consulta, genero) }

    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = consulta,
            onValueChange = { consulta = it },
            placeholder = { Text("Buscar por título, autor o ISBN") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (consulta.isNotEmpty()) {
                    IconButton(onClick = { consulta = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Limpiar búsqueda")
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                teclado?.hide()
                foco.clearFocus()
            }),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ChipGenero("Todos", seleccionado = genero == null) { genero = null }
            }
            items(generosDisponibles) { g ->
                ChipGenero(g.etiqueta, seleccionado = genero == g) {
                    genero = if (genero == g) null else g
                }
            }
        }

        Text(
            text = if (libros.size == 1) "1 libro encontrado" else "${libros.size} libros encontrados",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (libros.isEmpty()) {
            SinResultados()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(libros, key = { it.id }) { libro ->
                    LibroCard(libro, onClick = { idSeleccionado = libro.id })
                }
            }
        }
    }

    libreria.listarLibros().firstOrNull { it.id == idSeleccionado }?.let { libro ->
        DetalleLibroDialog(libro, onDismiss = { idSeleccionado = null })
    }
}

@Composable
private fun ChipGenero(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
        leadingIcon = if (seleccionado) {
            { Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(FilterChipDefaults.IconSize)) }
        } else null
    )
}

@Composable
private fun LibroCard(libro: Libro, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Portada(libro)
            Column(Modifier.weight(1f)) {
                Text(
                    libro.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(libro.autor.nombreCompleto(), style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${libro.editorial.nombre} · ${libro.anioPublicacion}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SuggestionChip(
                        onClick = onClick,
                        label = { Text(libro.genero.etiqueta) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        border = null
                    )
                    Spacer(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            formatearPrecio(libro.precioVenta()),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        IndicadorStock(libro.stock)
                    }
                }
            }
        }
    }
}

private val coloresPortada = listOf(
    Color(0xFF8B4A33), Color(0xFF2E5E4E), Color(0xFF3B4A7A), Color(0xFF6B5B2E),
    Color(0xFF7A3B5E), Color(0xFF2E5A7A), Color(0xFF5E3B7A)
)

/** Portada generada: color según el id e iniciales de las dos primeras palabras del título. */
@Composable
private fun Portada(libro: Libro) {
    val palabras = libro.titulo.split(" ").filter { it.isNotBlank() }
    val iniciales = if (palabras.size >= 2) {
        "${palabras[0].first()}${palabras[1].first()}"
    } else {
        libro.titulo.take(2)
    }.uppercase()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(68.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(coloresPortada[libro.id % coloresPortada.size])
    ) {
        // lomo del libro
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(6.dp)
                .background(Color.Black.copy(alpha = 0.18f))
        )
        Text(
            iniciales,
            color = Color.White,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun IndicadorStock(stock: Int) {
    val (texto, color) = when {
        stock == 0 -> "● Agotado" to RojoStock
        stock <= 5 -> "● Quedan $stock" to NaranjaStock
        else -> "● $stock en stock" to VerdeStock
    }
    Text(texto, color = color, style = MaterialTheme.typography.labelMedium)
}

@Composable
private fun SinResultados() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp)
    ) {
        Icon(
            Icons.Filled.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(56.dp)
        )
        Text(
            "No se encontraron libros",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun DetalleLibroDialog(libro: Libro, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null) },
        title = { Text(libro.titulo, fontFamily = FontFamily.Serif) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FilaDetalle("Autor", "${libro.autor.nombreCompleto()} (${libro.autor.nacionalidad})")
                FilaDetalle("Editorial", "${libro.editorial.nombre} (${libro.editorial.pais})")
                FilaDetalle(
                    "Año",
                    libro.anioPublicacion.toString() +
                        if (libro.esNovedad(LibreriaRepositorio.ANIO_ACTUAL)) " · ¡Novedad!" else ""
                )
                FilaDetalle("Género", libro.genero.etiqueta)
                FilaDetalle("Páginas", libro.paginas.toString())
                FilaDetalle("ISBN", libro.isbn)
                FilaDetalle("Precio", formatearPrecio(libro.precioVenta()))
                FilaDetalle("Stock", if (libro.hayStock()) "${libro.stock} unidades" else "Agotado")
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } }
    )
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Row {
        Text(
            "$etiqueta: ",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(valor, style = MaterialTheme.typography.bodyMedium)
    }
}

fun formatearPrecio(valor: Double): String = String.format(Locale.US, "Bs %.2f", valor)

@Preview(showBackground = true)
@Composable
private fun LibrosScreenPreview() {
    LibreriaTheme { LibrosScreen(LibreriaRepositorio.crearLibreria()) }
}
