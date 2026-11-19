"""
Checkpoint 4 - Genera el diseño (wireframes) de las dos pantallas de la app "Librería"
indicando, con marcadores numerados, qué componente de Jetpack Compose se usa en cada parte.

Salida:
    docs/checkpoint4/pantalla1_libros.drawio  + .png
    docs/checkpoint4/pantalla2_autores.drawio + .png

Ejecutar desde la raíz del repositorio:
    python scripts/generar_wireframes.py
"""
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from drawio_lib import Diagrama  # noqa: E402

RAIZ = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SALIDA = os.path.join(RAIZ, "docs", "checkpoint4")

# Colores de la app (ver ui/theme/Theme.kt)
CUERO = "#5D2E1F"
PAPEL = "#FFF8F0"
PAPEL_OSCURO = "#F3E6D8"
TARJETA = "#F8ECE2"
DORADO = "#C8902E"
VERDE = "#2E7D32"
NARANJA = "#E65100"
ROJO = "#C62828"
GRIS = "#53433E"
MARCADOR = "#1565C0"

# Teléfono
PX, PY, PW, PH = 40, 90, 380, 800
X0, W0 = PX + 12, PW - 24  # área de contenido


def rect(d, x, y, w, h, fill="#ffffff", stroke="#999999", texto="", extra="", html_crudo=False):
    return d.caja(texto, x, y, w, h, color=(fill, stroke), estilo_extra=extra, html_crudo=html_crudo)


def txt(d, s, x, y, w, h, extra=""):
    return d.texto(s, x, y, w, h, estilo_extra=f"align=left;verticalAlign=middle;spacing=0;{extra}",
                   html_crudo=True)


def marcador(d, n, x, y):
    d.caja(str(n), x, y, 24, 24, color=(MARCADOR, "#ffffff"),
           estilo_extra="shape=ellipse;perimeter=ellipsePerimeter;fontColor=#ffffff;fontStyle=1;"
                        "fontSize=12;strokeWidth=2;")


def leyenda(d, items, x, y, w=560):
    d.texto("<b style='font-size:15px'>Componentes de Jetpack Compose</b>", x, y, w, 26,
            estilo_extra="align=left;verticalAlign=middle;", html_crudo=True)
    y += 34
    for n, (comp, desc) in enumerate(items, start=1):
        marcador(d, n, x, y + 4)
        d.texto(f"<b style='font-family:Consolas'>{comp}</b><br><span style='color:#555'>{desc}</span>",
                x + 34, y, w - 34, 40, estilo_extra="align=left;verticalAlign=top;fontSize=12;",
                html_crudo=True)
        y += 46
    return y


def telefono(d, titulo, subtitulo):
    # marco del teléfono (Scaffold ocupa toda la pantalla)
    rect(d, PX - 8, PY - 8, PW + 16, PH + 16, fill="#222222", stroke="#222222", extra="rounded=1;arcSize=6;")
    rect(d, PX, PY, PW, PH, fill=PAPEL, stroke="#222222", extra="rounded=1;arcSize=4;")
    # barra de estado
    txt(d, "<span style='font-size:11px;color:#333'>&nbsp;&nbsp;9:41</span>", PX + 8, PY + 4, 80, 18)
    txt(d, "<span style='font-size:11px;color:#333'>▾ ▮ 100%</span>", PX + PW - 80, PY + 4, 70, 18)
    # TopAppBar
    rect(d, PX, PY + 26, PW, 62, fill=CUERO, stroke=CUERO)
    txt(d, f"<b style='color:#fff;font-size:18px;font-family:Georgia'>{titulo}</b><br>"
           f"<span style='color:#FFDBCF;font-size:12px'>{subtitulo}</span>", PX + 16, PY + 30, 300, 54)
    txt(d, "<span style='color:#fff;font-size:20px'>📚</span>", PX + PW - 44, PY + 42, 30, 30)


def barra_navegacion(d, seleccionado):
    y = PY + PH - 74
    rect(d, PX, y, PW, 74, fill=PAPEL_OSCURO, stroke=PAPEL_OSCURO, extra="rounded=0;")
    for i, (icono, etiqueta) in enumerate([("📖", "Libros"), ("👤", "Autores")]):
        cx = PX + PW * (0.25 + 0.5 * i)
        if i == seleccionado:
            rect(d, cx - 32, y + 10, 64, 30, fill="#FFDBCF", stroke="#FFDBCF", extra="rounded=1;arcSize=50;")
        txt(d, f"<div style='text-align:center;font-size:18px'>{icono}</div>", cx - 32, y + 10, 64, 30,
            "align=center;")
        peso = "bold" if i == seleccionado else "normal"
        txt(d, f"<div style='text-align:center;font-size:12px;font-weight:{peso}'>{etiqueta}</div>",
            cx - 40, y + 44, 80, 18, "align=center;")
    return y


def titulo_lamina(d, titulo, sub):
    d.texto(f"<b style='font-size:22px'>{titulo}</b><br><span style='font-size:13px'>{sub}</span>",
            20, 10, 900, 56, estilo_extra="align=left;verticalAlign=top;", html_crudo=True)


# =====================================================================
# Pantalla 1 - Catálogo de libros
# =====================================================================
def pantalla_libros():
    d = Diagrama("Pantalla 1 - Libros")
    titulo_lamina(d, "Pantalla 1 — Catálogo de libros",
                  "Lista objetos <b>Libro</b> (con su <b>Autor</b>, <b>Editorial</b> y <b>Genero</b>) · "
                  "búsqueda por texto y filtro por género")
    telefono(d, "Librería Gutenberg", "Catálogo de libros")

    # Buscador
    y = PY + 100
    rect(d, X0, y, W0, 46, fill="#ffffff", stroke="#8D7168", extra="rounded=1;arcSize=20;")
    txt(d, "<span style='color:#666;font-size:13px'>🔍&nbsp;&nbsp;Buscar por título, autor o ISBN</span>",
        X0 + 12, y + 8, W0 - 24, 30)

    # Chips de género
    y += 58
    x = X0
    for i, (g, w) in enumerate([("Todos", 62), ("Novela", 70), ("Cuento", 68), ("Fantasía", 80), ("Ciencia…", 66)]):
        sel = i == 0
        rect(d, x, y, w, 32, fill="#FFDBCF" if sel else "#ffffff", stroke="#8D7168",
             texto=("✓ " if sel else "") + g, extra="rounded=1;arcSize=25;fontSize=12;")
        x += w + 8
        if x > X0 + W0 - 10:
            break

    # Contador
    y += 42
    txt(d, "<span style='font-size:12px;color:#53433E'>14 libros encontrados</span>", X0 + 2, y, 200, 20)

    # Tarjetas (LazyColumn)
    y += 26
    lista_y = y
    libros = [
        ("CA", "#8B4A33", "Cien años de soledad", "Gabriel García Márquez", "Editorial Sudamericana · 1967",
         "Novela", "Bs 150.00", "● 12 en stock", VERDE),
        ("EH", "#2E5E4E", "El Hobbit", "J. R. R. Tolkien", "Minotauro · 1937", "Fantasía", "Bs 120.00",
         "● 20 en stock", VERDE),
        ("SA", "#3B4A7A", "El Señor de los Anillos", "J. R. R. Tolkien", "Minotauro · 1954", "Fantasía",
         "Bs 280.00", "● Quedan 3", NARANJA),
        ("RG", "#6B5B2E", "Rebelión en la granja", "George Orwell", "Planeta · 1945", "Ensayo", "Bs 85.00",
         "● Agotado", ROJO),
    ]
    tarjetas = []
    limite = PY + PH - 80
    for ini, color, titulo, autor, edit, genero, precio, stock, cstock in libros:
        h = 118
        if y + 40 > limite:
            break
        visible = min(h, limite - y)
        tarjetas.append(rect(d, X0, y, W0, visible, fill=TARJETA, stroke="#E0CFC2", extra="rounded=1;arcSize=8;"))
        if visible >= 60:
            rect(d, X0 + 10, y + 10, 66, min(98, visible - 10), fill=color, stroke=color, texto=ini,
                 extra="rounded=1;arcSize=8;fontColor=#ffffff;fontStyle=1;fontSize=20;fontFamily=Georgia;")
            txt(d, f"<b style='font-size:15px;font-family:Georgia'>{titulo}</b>", X0 + 86, y + 8, 260, 22)
            txt(d, f"<span style='font-size:13px'>{autor}</span>", X0 + 86, y + 30, 260, 18)
            txt(d, f"<span style='font-size:11px;color:#777'>{edit}</span>", X0 + 86, y + 49, 260, 16)
        if visible == h:
            rect(d, X0 + 86, y + 76, 72, 26, fill="#FFE0B2", stroke="#FFE0B2", texto=genero,
                 extra="rounded=1;arcSize=30;fontSize=11;")
            txt(d, f"<b style='font-size:15px;color:{CUERO}'>{precio}</b>", X0 + 250, y + 70, 100, 20)
            txt(d, f"<span style='font-size:11px;color:{cstock}'>{stock}</span>", X0 + 250, y + 90, 100, 16)
        y += h + 10

    barra_navegacion(d, 0)

    # Marcadores sobre el wireframe
    marcador(d, 1, PX - 20, PY + 300)            # Scaffold
    marcador(d, 2, PX + PW - 12, PY + 40)        # TopAppBar
    marcador(d, 3, PX + PW - 12, PY + 111)       # OutlinedTextField
    marcador(d, 4, PX + PW - 12, PY + 162)       # LazyRow + FilterChip
    marcador(d, 5, X0 + 150, PY + 202)           # Text contador
    marcador(d, 6, PX - 20, lista_y + 40)        # LazyColumn
    marcador(d, 7, PX + PW - 12, lista_y + 10)   # Card
    marcador(d, 8, X0 + 4, lista_y + 4)          # Box portada
    marcador(d, 9, X0 + 160, lista_y + 78)       # SuggestionChip
    marcador(d, 10, PX + PW - 12, lista_y + 84)  # Text precio/stock
    marcador(d, 11, PX + PW - 12, PY + PH - 50)  # NavigationBar

    yl = leyenda(d, [
        ("Scaffold", "Estructura base: topBar + bottomBar + contenido (innerPadding)."),
        ("TopAppBar", "Título de la librería y subtítulo de la pantalla; colores del tema (cuero)."),
        ("OutlinedTextField + Icon(Search)", "Búsqueda en vivo por título, autor o ISBN → Libreria.buscarLibros()."),
        ("LazyRow + FilterChip", "Filtro por enum Genero; \"Todos\" quita el filtro. Chip seleccionado con ✓."),
        ("Text", "Contador de resultados (\"14 libros encontrados\")."),
        ("LazyColumn (items, key = id)", "Lista desplazable y perezosa de objetos Libro."),
        ("Card (onClick) + Row / Column", "Tarjeta de cada libro; al tocarla abre un AlertDialog con el detalle."),
        ("Box + Text", "Portada generada: color + iniciales del título (RoundedCornerShape)."),
        ("SuggestionChip", "Etiqueta del género del libro (Genero.etiqueta)."),
        ("Text (color según stock)", "Precio (precioVenta()) y stock: verde / naranja (≤5) / rojo (agotado)."),
        ("NavigationBar + NavigationBarItem", "Navegación entre las dos pantallas: Libros y Autores."),
        ("AlertDialog", "Detalle del libro: ISBN, páginas, editorial y país, año y novedad."),
    ], PX + PW + 60, PY - 10)

    # Mini boceto del AlertDialog (12)
    dx, dy = PX + PW + 94, yl + 4
    rect(d, dx, dy, 300, 150, fill="#ffffff", stroke="#999999", extra="rounded=1;arcSize=8;shadow=1;")
    txt(d, "<b style='font-size:14px;font-family:Georgia'>Cien años de soledad</b>", dx + 16, dy + 10, 270, 22)
    txt(d, "<span style='font-size:11px;color:#555'>ISBN 9780307474728 · 471 páginas<br>"
           "Editorial Sudamericana (Argentina) · 1967<br>Autor: Gabriel García Márquez (Colombia)</span>",
        dx + 16, dy + 36, 270, 60)
    txt(d, f"<b style='font-size:12px;color:{CUERO}'>CERRAR</b>", dx + 230, dy + 114, 60, 22)
    marcador(d, 12, dx - 12, dy - 10)
    return d


# =====================================================================
# Pantalla 2 - Autores
# =====================================================================
def pantalla_autores():
    d = Diagrama("Pantalla 2 - Autores")
    titulo_lamina(d, "Pantalla 2 — Autores",
                  "Lista objetos <b>Autor</b> y, al expandir, sus objetos <b>Libro</b> "
                  "(Libreria.librosDeAutor)")
    telefono(d, "Autores", "7 autores registrados")

    y = PY + 100
    lista_y = y
    autores = [
        ("IA", "Isabel Allende", "Chile · n. 1942", "2 libros", True),
        ("JB", "Jorge Luis Borges", "Argentina · n. 1899", "2 libros", False),
        ("GG", "Gabriel García Márquez", "Colombia · n. 1927", "3 libros", False),
        ("GO", "George Orwell", "Reino Unido · n. 1903", "2 libros", False),
        ("EP", "Edmundo Paz Soldán", "Bolivia · n. 1967", "2 libros", False),
    ]
    limite = PY + PH - 80
    for ini, nombre, sub, n, expandido in autores:
        h = 236 if expandido else 94
        if y + 30 > limite:
            break
        visible = min(h, limite - y)
        rect(d, X0, y, W0, visible, fill="#ffffff", stroke="#E0CFC2", extra="rounded=1;arcSize=6;shadow=1;")
        if visible >= 94:
            rect(d, X0 + 12, y + 22, 48, 48, fill=CUERO, stroke=CUERO, texto=ini,
                 extra="shape=ellipse;perimeter=ellipsePerimeter;fontColor=#ffffff;fontStyle=1;fontSize=15;")
            txt(d, f"<b style='font-size:15px;font-family:Georgia'>{nombre}</b>", X0 + 72, y + 12, 260, 22)
            txt(d, f"<span style='font-size:12px;color:#666'>{sub}</span>", X0 + 72, y + 36, 240, 18)
            rect(d, X0 + 72, y + 62, 62, 20, fill=DORADO, stroke=DORADO, texto=n,
                 extra="rounded=1;arcSize=50;fontSize=11;fontColor=#ffffff;fontStyle=1;")
            txt(d, f"<span style='font-size:16px'>{'▴' if expandido else '▾'}</span>", X0 + W0 - 30, y + 34, 20, 24)
        if expandido:
            rect(d, X0 + 12, y + 94, W0 - 24, 1, fill="#E0CFC2", stroke="#E0CFC2")
            txt(d, "<span style='font-size:12px;color:#444'><i>Una de las novelistas en español más leídas "
                   "del mundo.</i></span>", X0 + 14, y + 102, W0 - 28, 36,
                "whiteSpace=wrap;verticalAlign=top;")
            txt(d, "<b style='font-size:12px'>Libros en la librería</b>", X0 + 14, y + 142, 200, 18)
            for j, (t, a, p) in enumerate([("La casa de los espíritus", "1982", "Bs 140.00"),
                                           ("El viento conoce mi nombre", "2025", "Bs 165.00")]):
                yy = y + 166 + j * 32
                rect(d, X0 + 14, yy, W0 - 28, 28, fill=TARJETA, stroke=TARJETA, extra="rounded=1;arcSize=20;")
                txt(d, f"<span style='font-size:12px'>📖 {t} <span style='color:#888'>({a})</span></span>",
                    X0 + 22, yy + 4, 250, 20)
                txt(d, f"<b style='font-size:12px;color:{CUERO}'>{p}</b>", X0 + W0 - 92, yy + 4, 76, 20)
        y += h + 10

    barra_navegacion(d, 1)

    marcador(d, 1, PX - 20, PY + 300)            # Scaffold
    marcador(d, 2, PX + PW - 12, PY + 40)        # TopAppBar
    marcador(d, 3, PX - 20, lista_y + 360)       # LazyColumn
    marcador(d, 4, PX + PW - 12, lista_y + 4)    # ElevatedCard
    marcador(d, 5, X0 + 2, lista_y + 4)          # Avatar
    marcador(d, 6, X0 + 300, lista_y + 34)       # Textos
    marcador(d, 7, X0 + 140, lista_y + 60)       # Badge
    marcador(d, 8, PX + PW - 12, lista_y + 40)   # Icono expandir
    marcador(d, 9, PX - 20, lista_y + 168)       # AnimatedVisibility
    marcador(d, 10, X0 + W0 - 6, lista_y + 84)   # HorizontalDivider
    marcador(d, 11, X0 + W0 - 6, lista_y + 168)  # ListItem libros
    marcador(d, 12, PX + PW - 12, PY + PH - 50)  # NavigationBar

    leyenda(d, [
        ("Scaffold", "Misma estructura que la pantalla 1 (comparten la NavigationBar)."),
        ("TopAppBar", "Título \"Autores\" y cantidad de autores registrados."),
        ("LazyColumn (items, key = id)", "Lista de objetos Autor ordenados por apellido (listarAutores())."),
        ("ElevatedCard (onClick)", "Tarjeta de cada autor; al tocarla se expande o contrae."),
        ("Box (CircleShape) + Text", "Avatar circular con las iniciales del autor (Persona.iniciales())."),
        ("Text / Column", "Nombre completo y nacionalidad · año de nacimiento."),
        ("Badge", "Cantidad de libros del autor (Libreria.librosDeAutor(autor).size)."),
        ("Icon (ExpandMore / ExpandLess)", "Indica si la tarjeta está expandida (rememberSaveable)."),
        ("AnimatedVisibility", "Animación al mostrar la biografía y los libros del autor."),
        ("HorizontalDivider", "Separa el encabezado del contenido expandido."),
        ("Row + Text (por cada Libro)", "Título, año y precio de cada libro del autor."),
        ("NavigationBar + NavigationBarItem", "Navegación entre las dos pantallas: Libros y Autores."),
    ], PX + PW + 60, PY - 10)
    return d


if __name__ == "__main__":
    for nombre, fabrica in [("pantalla1_libros", pantalla_libros), ("pantalla2_autores", pantalla_autores)]:
        diagrama = fabrica()
        ruta = os.path.join(SALIDA, f"{nombre}.drawio")
        diagrama.guardar(ruta)
        diagrama.exportar_png(ruta, os.path.join(SALIDA, f"{nombre}.png"))
