# Checkpoint 6 - Inicia un emulador, instala la app, la abre y toma capturas de las dos pantallas.
# Uso (desde la raíz del repositorio, después de scripts\compilar.ps1):
#   powershell -ExecutionPolicy Bypass -File scripts\ejecutar_emulador.ps1 [-Avd Medium_Phone_API_37.0]

param(
    [string]$Avd = "",
    [string]$Salida = "docs\checkpoint6"
)

$ErrorActionPreference = "Stop"
$raiz = Split-Path -Parent $PSScriptRoot
Set-Location $raiz

$sdk = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { "$env:LOCALAPPDATA\Android\Sdk" }
$adb = Join-Path $sdk "platform-tools\adb.exe"
$emulador = Join-Path $sdk "emulator\emulator.exe"
$apk = "app\build\outputs\apk\debug\app-debug.apk"
$paquete = "com.upb.libreria"

function Invoke-Adb { & $adb @args }

# 1. Emulador: usar uno conectado o iniciar el AVD indicado (o el primero disponible)
$conectado = (Invoke-Adb devices) -match "device$"
if (-not $conectado) {
    if (-not $Avd) { $Avd = (& $emulador -list-avds | Select-Object -First 1) }
    Write-Host "==> Iniciando emulador $Avd ..."
    Start-Process -FilePath $emulador -ArgumentList "-avd", $Avd, "-no-snapshot-save", "-no-boot-anim" -WindowStyle Minimized
    Invoke-Adb wait-for-device
    do { Start-Sleep -Seconds 3 } until ((Invoke-Adb shell getprop sys.boot_completed).Trim() -eq "1")
}
Write-Host "==> Dispositivo listo: $((Invoke-Adb shell getprop ro.product.model).Trim()) (Android $((Invoke-Adb shell getprop ro.build.version.release).Trim()))"

# 2. Instalar y abrir
Invoke-Adb install -r $apk
Invoke-Adb shell am force-stop $paquete
Invoke-Adb shell am start -W -n "$paquete/.MainActivity"
Start-Sleep -Seconds 6

# 3. Capturas de pantalla
New-Item -ItemType Directory -Force $Salida | Out-Null
function Save-Captura($nombre) {
    Invoke-Adb shell screencap -p /sdcard/captura.png
    Invoke-Adb pull /sdcard/captura.png (Join-Path $Salida $nombre) | Out-Null
    Write-Host "    captura -> $Salida\$nombre"
}

# Toca el centro del primer elemento de la UI cuyo texto empiece con $texto (usa uiautomator)
function Invoke-TocarTexto($texto) {
    $xml = (Invoke-Adb exec-out uiautomator dump /dev/tty) -join ""
    $patron = 'text="' + [regex]::Escape($texto) + '[^"]*"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"'
    if ($xml -match $patron) {
        $x = ([int]$Matches[1] + [int]$Matches[3]) / 2
        $y = ([int]$Matches[2] + [int]$Matches[4]) / 2
        Invoke-Adb shell input tap $x $y
        Start-Sleep -Seconds 2
        return $true
    }
    Write-Warning "No se encontró '$texto' en la pantalla"
    return $false
}

# Pantalla 1: lista de libros
Save-Captura "pantalla1_libros.png"

# Pantalla 1: detalle de un libro (AlertDialog). Prefijo ASCII de "Cien años de soledad".
if (Invoke-TocarTexto "Cien a") {
    Save-Captura "pantalla1_detalle.png"
    Invoke-TocarTexto "Cerrar" | Out-Null
}

# Pantalla 1: búsqueda por autor
if (Invoke-TocarTexto "Buscar por") {
    Invoke-Adb shell input text "Tolkien"
    Invoke-Adb shell input keyevent 66    # ENTER: accion "Buscar" (oculta el teclado)
    Start-Sleep -Seconds 2
    Save-Captura "pantalla1_busqueda.png"
}

# Pantalla 2: autores (pestaña de la NavigationBar)
if (Invoke-TocarTexto "Autores") {
    Save-Captura "pantalla2_autores.png"
}
Write-Host "==> Listo"
