package com.upb.libreria.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upb.libreria.data.LibreriaRepositorio
import com.upb.libreria.model.Libreria
import com.upb.libreria.ui.screens.AutoresScreen
import com.upb.libreria.ui.screens.LibrosScreen
import com.upb.libreria.ui.theme.LibreriaTheme

/** Pantallas de la aplicación (destinos de la NavigationBar). */
enum class Pantalla(val etiqueta: String, val icono: ImageVector) {
    LIBROS("Libros", Icons.AutoMirrored.Filled.MenuBook),
    AUTORES("Autores", Icons.Filled.Person)
}

/** Raíz de la interfaz: Scaffold con TopAppBar, NavigationBar y la pantalla seleccionada. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibreriaApp(libreria: Libreria = LibreriaRepositorio.libreria) {
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.LIBROS) }

    Scaffold(
        topBar = {
            val (titulo, subtitulo) = when (pantalla) {
                Pantalla.LIBROS -> libreria.nombre to "Catálogo de libros"
                Pantalla.AUTORES -> "Autores" to "${libreria.listarAutores().size} autores registrados"
            }
            TopAppBar(
                title = {
                    Column {
                        Text(titulo, style = MaterialTheme.typography.titleLarge)
                        Text(
                            subtitulo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    Icon(
                        Icons.Filled.LocalLibrary,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                Pantalla.entries.forEach { destino ->
                    NavigationBarItem(
                        selected = destino == pantalla,
                        onClick = { pantalla = destino },
                        icon = { Icon(destino.icono, contentDescription = null) },
                        label = { Text(destino.etiqueta) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (pantalla) {
            Pantalla.LIBROS -> LibrosScreen(libreria, modifier)
            Pantalla.AUTORES -> AutoresScreen(libreria, modifier)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LibreriaAppPreview() {
    LibreriaTheme { LibreriaApp(LibreriaRepositorio.crearLibreria()) }
}
