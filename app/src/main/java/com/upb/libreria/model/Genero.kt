package com.upb.libreria.model

/** Géneros literarios disponibles en el catálogo. */
enum class Genero(val etiqueta: String) {
    NOVELA("Novela"),
    CUENTO("Cuento"),
    FANTASIA("Fantasía"),
    CIENCIA_FICCION("Ciencia ficción"),
    POESIA("Poesía"),
    HISTORIA("Historia"),
    INFANTIL("Infantil"),
    ENSAYO("Ensayo")
}
