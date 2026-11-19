# Checkpoint 3 – Scripts usados en el examen

| Script | Para qué sirve | Cómo se ejecuta (desde la raíz) |
|---|---|---|
| [`prompts_ia.md`](prompts_ia.md) | Prompt(s) de IA usados para resolver el examen | — |
| [`drawio_lib.py`](drawio_lib.py) | Mini librería: genera archivos `.drawio` (formas, clases UML, relaciones) y los exporta a PNG con el visor oficial de diagrams.net en Edge/Chrome *headless* | (módulo) |
| [`generar_diagrama_clases.py`](generar_diagrama_clases.py) | Checkpoint 1: diagrama de clases UML | `python scripts/generar_diagrama_clases.py` |
| [`generar_wireframes.py`](generar_wireframes.py) | Checkpoint 4: diseño de las dos pantallas | `python scripts/generar_wireframes.py` |
| [`compilar.ps1`](compilar.ps1) | Checkpoint 6: `clean assembleDebug testDebugUnitTest` | `powershell -ExecutionPolicy Bypass -File scripts\compilar.ps1` |
| [`ejecutar_emulador.ps1`](ejecutar_emulador.ps1) | Checkpoint 6: inicia el emulador, instala el APK, abre la app y toma capturas de ambas pantallas | `powershell -ExecutionPolicy Bypass -File scripts\ejecutar_emulador.ps1` |

Requisitos: Python 3 (solo librería estándar), Microsoft Edge o Google Chrome (para exportar PNG; requiere internet
para cargar el visor de diagrams.net), Android Studio / Android SDK con un AVD creado.

Los archivos `.drawio` generados se pueden abrir y editar en <https://app.diagrams.net>.
