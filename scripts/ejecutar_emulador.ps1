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
Start-Sleep -Seconds 3

# 3. Capturas de pantalla
New-Item -ItemType Directory -Force $Salida | Out-Null
function Save-Captura($nombre) {
    Invoke-Adb shell screencap -p /sdcard/captura.png
    Invoke-Adb pull /sdcard/captura.png (Join-Path $Salida $nombre) | Out-Null
    Write-Host "    captura -> $Salida\$nombre"
}
Save-Captura "pantalla1_libros.png"

# Cambiar a la pestaña "Autores" de la barra de navegación inferior
$xml = Invoke-Adb exec-out uiautomator dump /dev/tty
if ($xml -match 'text="Autores"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"') {
    $x = ([int]$Matches[1] + [int]$Matches[3]) / 2
    $y = ([int]$Matches[2] + [int]$Matches[4]) / 2
    Invoke-Adb shell input tap $x $y
    Start-Sleep -Seconds 2
    Save-Captura "pantalla2_autores.png"
} else {
    Write-Warning "No se encontró la pestaña 'Autores' en la jerarquía de la UI"
}
Write-Host "==> Listo"
