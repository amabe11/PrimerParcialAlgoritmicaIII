package com.upb.libreria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.upb.libreria.data.LibreriaRepositorio
import com.upb.libreria.ui.theme.LibreriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val libreria = LibreriaRepositorio.libreria
        setContent {
            LibreriaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Text(
                        text = "${libreria.nombre}: ${libreria.listarLibros().size} libros, " +
                            "${libreria.listarAutores().size} autores",
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
