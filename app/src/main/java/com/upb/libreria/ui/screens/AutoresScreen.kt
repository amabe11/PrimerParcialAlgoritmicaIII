package com.upb.libreria.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upb.libreria.data.LibreriaRepositorio
import com.upb.libreria.model.Autor
import com.upb.libreria.model.Libreria
import com.upb.libreria.model.Libro
import com.upb.libreria.ui.theme.LibreriaTheme

/** Pantalla 2: lista de objetos [Autor]; al expandir uno se ven sus objetos [Libro]. */
@Composable
fun AutoresScreen(libreria: Libreria, modifier: Modifier = Modifier) {
    val autores = remember(libreria) { libreria.listarAutores() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(autores, key = { it.id }) { autor ->
            AutorCard(
                autor = autor,
                libros = libreria.librosDeAutor(autor),
                expandidoInicial = autor.id == autores.first().id
            )
        }
    }
}

@Composable
private fun AutorCard(autor: Autor, libros: List<Libro>, expandidoInicial: Boolean) {
    var expandido by rememberSaveable(autor.id) { mutableStateOf(expandidoInicial) }

    ElevatedCard(
        onClick = { expandido = !expandido },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(autor.iniciales())
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(autor.nombreCompleto(), style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${autor.nacionalidad} · n. ${autor.anioNacimiento}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Text(
                            if (libros.size == 1) "1 libro" else "${libros.size} libros",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Icon(
                    if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Contraer" else "Expandir"
                )
            }

            AnimatedVisibility(visible = expandido) {
                Column {
                    HorizontalDivider(Modifier.padding(vertical = 12.dp))
                    Text(
                        autor.biografia,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        "Libros en la librería",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                    )
                    libros.forEach { libro -> FilaLibro(libro) }
                }
            }
        }
    }
}

@Composable
private fun Avatar(iniciales: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            iniciales,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun FilaLibro(libro: Libro) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            Icons.AutoMirrored.Filled.MenuBook,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            libro.titulo,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            " (${libro.anioPublicacion})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(8.dp))
        Text(
            formatearPrecio(libro.precioVenta()),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AutoresScreenPreview() {
    LibreriaTheme { AutoresScreen(LibreriaRepositorio.crearLibreria()) }
}
