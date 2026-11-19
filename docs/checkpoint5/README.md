# Checkpoint 5 – Interfaz con Jetpack Compose

Las dos pantallas del [diseño del Checkpoint 4](../checkpoint4) están implementadas con Jetpack Compose (Material 3).

| Archivo | Contenido |
|---|---|
| [`MainActivity.kt`](../../app/src/main/java/com/upb/libreria/MainActivity.kt) | `setContent { LibreriaTheme { LibreriaApp() } }` con `enableEdgeToEdge()`. |
| [`ui/LibreriaApp.kt`](../../app/src/main/java/com/upb/libreria/ui/LibreriaApp.kt) | `Scaffold` + `TopAppBar` + `NavigationBar`; enum `Pantalla` (LIBROS, AUTORES) guardado con `rememberSaveable`. |
| [`ui/screens/LibrosScreen.kt`](../../app/src/main/java/com/upb/libreria/ui/screens/LibrosScreen.kt) | **Pantalla 1:** buscador (`OutlinedTextField`), filtros (`LazyRow` + `FilterChip`), lista (`LazyColumn` de `Card`), portada (`Box`), `SuggestionChip`, indicador de stock y `AlertDialog` de detalle. |
| [`ui/screens/AutoresScreen.kt`](../../app/src/main/java/com/upb/libreria/ui/screens/AutoresScreen.kt) | **Pantalla 2:** `LazyColumn` de `ElevatedCard` expandibles con avatar (`CircleShape`), `Badge`, `AnimatedVisibility`, `HorizontalDivider` y la fila de cada libro. |
| [`ui/theme/Theme.kt`](../../app/src/main/java/com/upb/libreria/ui/theme/Theme.kt) | `LibreriaTheme`: esquema de colores claro/oscuro y tipografía. |
| [`data/LibreriaRepositorio.kt`](../../app/src/main/java/com/upb/libreria/data/LibreriaRepositorio.kt) | Construye la `Libreria` de ejemplo (7 autores, 14 libros, 5 artículos, 2 clientes, 2 empleados y 2 ventas). |

## Objetos del modelo que se visualizan

- **Pantalla 1 – Libros:** `Libro` (título, ISBN, páginas, año, `precioVenta()`, `stock`, `esNovedad()`) junto con
  su `Autor`, su `Editorial` y su `Genero`. Usa `Libreria.buscarLibros(texto, genero)`.
- **Pantalla 2 – Autores:** `Autor` (`nombreCompleto()`, `iniciales()`, nacionalidad, año de nacimiento, biografía)
  y la lista de sus `Libro` con `Libreria.librosDeAutor(autor)`.

El stock que se ve ya refleja las ventas de ejemplo (por ejemplo, *Cien años de soledad* empieza con 12 y muestra 11
porque la venta n.º 1 descontó una unidad con `Venta.agregarProducto`).

Cada pantalla tiene su `@Preview` para verla en Android Studio sin ejecutar la app.
