"""
Mini librería para generar diagramas draw.io (.drawio) desde Python
y exportarlos a PNG usando el visor oficial de diagrams.net en Edge headless.

Uso:
    from drawio_lib import Diagrama
    d = Diagrama()
    a = d.caja("Hola", 10, 10, 120, 40)
    d.guardar("salida.drawio")
    d.exportar_png("salida.drawio", "salida.png")
"""
import html
import json
import os
import shutil
import subprocess
import tempfile
import xml.etree.ElementTree as ET

EDGE_PATHS = [
    r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
    r"C:\Program Files\Microsoft\Edge\Application\msedge.exe",
    r"C:\Program Files\Google\Chrome\Application\chrome.exe",
]

# Paletas de color (relleno, borde) estilo draw.io
AZUL = ("#dae8fc", "#6c8ebf")
VERDE = ("#d5e8d4", "#82b366")
NARANJA = ("#ffe6cc", "#d79b00")
MORADO = ("#e1d5e7", "#9673a6")
AMARILLO = ("#fff2cc", "#d6b656")
GRIS = ("#f5f5f5", "#666666")
ROJO = ("#f8cecc", "#b85450")
BLANCO = ("#ffffff", "#333333")

# Estilos de relaciones UML
HERENCIA = "endArrow=block;endFill=0;endSize=12;"
REALIZACION = "endArrow=block;endFill=0;endSize=12;dashed=1;"
ASOCIACION = "endArrow=open;endFill=0;endSize=10;"
AGREGACION = "startArrow=diamondThin;startFill=0;startSize=14;endArrow=open;endFill=0;endSize=10;"
COMPOSICION = "startArrow=diamondThin;startFill=1;startSize=14;endArrow=open;endFill=0;endSize=10;"
DEPENDENCIA = "endArrow=open;endFill=0;endSize=10;dashed=1;"


def _html_lineas(lineas):
    return "<br>".join(html.escape(l, quote=False) for l in lineas)


class Diagrama:
    def __init__(self, nombre="Diagrama"):
        self.nombre = nombre
        self.celdas = []
        self._id = 2
        self.max_x = 0
        self.max_y = 0

    # ------------------------------------------------------------------ util
    def _nuevo_id(self):
        self._id += 1
        return f"c{self._id}"

    def _celda(self, valor, estilo, x, y, w, h, padre="1", vertex=True, extra=None):
        cid = self._nuevo_id()
        attrs = {"id": cid, "value": valor, "style": estilo, "parent": padre}
        if vertex:
            attrs["vertex"] = "1"
        if extra:
            attrs.update(extra)
        self.celdas.append(("vertex", attrs, (x, y, w, h)))
        if padre == "1":
            self.max_x = max(self.max_x, x + w)
            self.max_y = max(self.max_y, y + h)
        return cid

    # ---------------------------------------------------------------- formas
    def caja(self, texto, x, y, w, h, color=BLANCO, estilo_extra="", padre="1", html_crudo=False):
        valor = texto if html_crudo else _html_lineas(texto.split("\n"))
        estilo = (f"rounded=0;whiteSpace=wrap;html=1;fillColor={color[0]};"
                  f"strokeColor={color[1]};{estilo_extra}")
        return self._celda(valor, estilo, x, y, w, h, padre)

    def texto(self, texto, x, y, w, h, estilo_extra="", padre="1", html_crudo=False):
        valor = texto if html_crudo else _html_lineas(texto.split("\n"))
        estilo = f"text;html=1;whiteSpace=wrap;strokeColor=none;fillColor=none;{estilo_extra}"
        return self._celda(valor, estilo, x, y, w, h, padre)

    def clase_uml(self, nombre, atributos, metodos, x, y, w=280, color=AZUL,
                  estereotipo=None, abstracta=False, fuente=12):
        """Crea una clase UML (encabezado, atributos y métodos) al estilo draw.io."""
        lh = fuente + 6
        nombre_html = f"<i>{html.escape(nombre)}</i>" if abstracta else html.escape(nombre)
        if estereotipo:
            encabezado = f"<span style='font-weight:normal'>«{html.escape(estereotipo)}»</span><br>{nombre_html}"
            start = 42
        else:
            encabezado = nombre_html
            start = 28
        h_attr = max(1, len(atributos)) * lh + 8 if atributos else 10
        h_met = max(1, len(metodos)) * lh + 8 if metodos else 10
        h = start + h_attr + 8 + h_met
        estilo = ("swimlane;fontStyle=1;align=center;verticalAlign=top;childLayout=stackLayout;"
                  f"horizontal=1;startSize={start};horizontalStack=0;resizeParent=1;resizeParentMax=0;"
                  "resizeLast=0;collapsible=0;marginBottom=0;whiteSpace=wrap;html=1;"
                  f"fillColor={color[0]};strokeColor={color[1]};fontSize={fuente + 1};swimlaneFillColor=#ffffff;")
        cid = self._celda(encabezado, estilo, x, y, w, h)
        estilo_txt = ("text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=6;"
                      "spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];"
                      f"portConstraint=eastwest;whiteSpace=wrap;html=1;fontSize={fuente};fontFamily=Consolas;")
        self._celda(_html_lineas(atributos), estilo_txt, 0, start, w, h_attr, padre=cid)
        self._celda("", "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;"
                        "spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];"
                        f"portConstraint=eastwest;strokeColor={color[1]};",
                    0, start + h_attr, w, 8, padre=cid)
        self._celda(_html_lineas(metodos), estilo_txt, 0, start + h_attr + 8, w, h_met, padre=cid)
        return cid

    def relacion(self, origen, destino, estilo, etiqueta="", mult_origen="", mult_destino="",
                 puntos=None, salida=None, entrada=None):
        """Relación entre dos celdas. salida/entrada = (x, y) relativos 0..1 para fijar puertos."""
        cid = self._nuevo_id()
        st = f"edgeStyle=orthogonalEdgeStyle;rounded=0;html=1;fontSize=11;{estilo}"
        if salida:
            st += f"exitX={salida[0]};exitY={salida[1]};exitDx=0;exitDy=0;"
        if entrada:
            st += f"entryX={entrada[0]};entryY={entrada[1]};entryDx=0;entryDy=0;"
        attrs = {"id": cid, "value": html.escape(etiqueta), "style": st, "parent": "1",
                 "edge": "1", "source": origen, "target": destino}
        self.celdas.append(("edge", attrs, puntos or []))
        lbl = "edgeLabel;resizable=0;html=1;fontSize=11;fontStyle=1;"
        if mult_origen:
            self.celdas.append(("elabel", {"id": self._nuevo_id(), "value": mult_origen,
                                           "style": lbl + "align=left;verticalAlign=bottom;",
                                           "vertex": "1", "connectable": "0", "parent": cid}, -0.85))
        if mult_destino:
            self.celdas.append(("elabel", {"id": self._nuevo_id(), "value": mult_destino,
                                           "style": lbl + "align=right;verticalAlign=bottom;",
                                           "vertex": "1", "connectable": "0", "parent": cid}, 0.85))
        return cid

    # ------------------------------------------------------------- serializar
    def xml(self):
        mxfile = ET.Element("mxfile", host="drawio", agent="drawio_lib.py", type="device")
        diagram = ET.SubElement(mxfile, "diagram", name=self.nombre, id="d1")
        model = ET.SubElement(diagram, "mxGraphModel", dx="1400", dy="900", grid="1", gridSize="10",
                              guides="1", tooltips="1", connect="1", arrows="1", fold="1", page="1",
                              pageScale="1", pageWidth=str(self.max_x + 40), pageHeight=str(self.max_y + 40),
                              math="0", shadow="0")
        root = ET.SubElement(model, "root")
        ET.SubElement(root, "mxCell", id="0")
        ET.SubElement(root, "mxCell", id="1", parent="0")
        for tipo, attrs, geo in self.celdas:
            cell = ET.SubElement(root, "mxCell", attrs)
            if tipo == "vertex":
                x, y, w, h = geo
                ET.SubElement(cell, "mxGeometry", x=str(x), y=str(y), width=str(w), height=str(h),
                              **{"as": "geometry"})
            elif tipo == "edge":
                g = ET.SubElement(cell, "mxGeometry", relative="1", **{"as": "geometry"})
                if geo:
                    arr = ET.SubElement(g, "Array", **{"as": "points"})
                    for px, py in geo:
                        ET.SubElement(arr, "mxPoint", x=str(px), y=str(py))
            elif tipo == "elabel":
                g = ET.SubElement(cell, "mxGeometry", x=str(geo), relative="1", **{"as": "geometry"})
                ET.SubElement(g, "mxPoint", **{"as": "offset"})
        ET.indent(mxfile, space="  ")
        return ET.tostring(mxfile, encoding="unicode")

    def guardar(self, ruta):
        os.makedirs(os.path.dirname(os.path.abspath(ruta)), exist_ok=True)
        with open(ruta, "w", encoding="utf-8") as f:
            f.write(self.xml())
        print(f"[drawio] guardado {ruta}")

    def exportar_png(self, ruta_drawio, ruta_png, escala=2, margen=30):
        exportar_png(ruta_drawio, ruta_png, self.max_x + margen, self.max_y + margen, escala)


def exportar_png(ruta_drawio, ruta_png, ancho, alto, escala=2):
    """Renderiza un .drawio a PNG con el visor de diagrams.net en un navegador headless."""
    navegador = next((p for p in EDGE_PATHS if os.path.exists(p)), None) or shutil.which("msedge")
    if not navegador:
        raise RuntimeError("No se encontró Edge/Chrome para exportar el PNG")
    with open(ruta_drawio, encoding="utf-8") as f:
        xml = f.read()
    cfg = {"nav": False, "toolbar": None, "resize": False, "lightbox": False, "xml": xml}
    pagina = ("<!doctype html><html><head><meta charset='utf-8'>"
              "<style>html,body{margin:0;padding:0;background:#ffffff}</style></head><body>"
              f"<div class='mxgraph' data-mxgraph=\"{html.escape(json.dumps(cfg), quote=True)}\"></div>"
              "<script src='https://viewer.diagrams.net/js/viewer-static.min.js'></script>"
              "</body></html>")
    tmp = tempfile.mkdtemp(prefix="drawio_")
    ruta_html = os.path.join(tmp, "render.html")
    with open(ruta_html, "w", encoding="utf-8") as f:
        f.write(pagina)
    ruta_png = os.path.abspath(ruta_png)
    os.makedirs(os.path.dirname(ruta_png), exist_ok=True)
    subprocess.run([navegador, "--headless=new", "--disable-gpu", "--hide-scrollbars",
                    f"--force-device-scale-factor={escala}", f"--window-size={int(ancho)},{int(alto)}",
                    "--virtual-time-budget=20000", f"--screenshot={ruta_png}",
                    "file:///" + ruta_html.replace("\\", "/")],
                   check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    shutil.rmtree(tmp, ignore_errors=True)
    print(f"[drawio] exportado {ruta_png}")
