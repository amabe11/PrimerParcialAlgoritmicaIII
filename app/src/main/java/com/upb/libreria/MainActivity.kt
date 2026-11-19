package com.upb.libreria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.upb.libreria.ui.LibreriaApp
import com.upb.libreria.ui.theme.LibreriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibreriaTheme {
                LibreriaApp()
            }
        }
    }
}
