# Prompts de IA usados en el examen

Herramienta: **Claude Code** (asistente de programación con IA en la terminal), trabajando sobre este repositorio.

## Prompt 1 — Enunciado completo

> Analiza el siguiente archivo, implementa todos los checkpoints, en los casos de estudio elige "Libreria".
> Caso especial: Para los checkpoints 1 y 4 se necesita que esten imagenes en el repositorio para cada checkpoint,
> se puede usar herramientas como draw.io

Archivo adjunto: `Primer PArcial IA.pdf` (enunciado del parcial con los 6 checkpoints).

## Qué hizo el asistente con ese prompt

1. Leyó el PDF del enunciado y eligió el caso de estudio **Librería**.
2. **Checkpoint 1:** diseñó las clases y escribió `generar_diagrama_clases.py`, que produce el diagrama UML como
   archivo **draw.io** editable y lo exporta a PNG.
3. **Checkpoint 2:** creó el proyecto Android (Gradle, AGP 9, Kotlin, Compose) e implementó las clases del modelo
   con pruebas unitarias.
4. **Checkpoint 3:** guardó en esta carpeta los scripts y prompts usados.
5. **Checkpoint 4:** escribió `generar_wireframes.py` para diseñar las dos pantallas en draw.io (PNG + .drawio)
   indicando los componentes de Jetpack Compose.
6. **Checkpoint 5:** implementó las dos pantallas con Jetpack Compose.
7. **Checkpoint 6:** compiló con `compilar.ps1`, ejecutó en el emulador con `ejecutar_emulador.ps1` y guardó las capturas.
