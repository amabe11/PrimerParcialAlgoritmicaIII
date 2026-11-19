# Checkpoint 6 - Compila el proyecto y ejecuta las pruebas unitarias.
# Uso (desde la raíz del repositorio):
#   powershell -ExecutionPolicy Bypass -File scripts\compilar.ps1

$ErrorActionPreference = "Stop"
$raiz = Split-Path -Parent $PSScriptRoot
Set-Location $raiz

if (-not $env:JAVA_HOME -and (Test-Path "C:\Program Files\Android\Android Studio\jbr")) {
    $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
}
if (-not (Test-Path "local.properties")) {
    $sdk = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { "$env:LOCALAPPDATA\Android\Sdk" }
    "sdk.dir=$($sdk -replace '\\', '\\')" | Out-File -Encoding ascii local.properties
}

Write-Host "==> Compilando APK debug y ejecutando pruebas unitarias..."
& .\gradlew.bat clean assembleDebug testDebugUnitTest --console=plain
if ($LASTEXITCODE -ne 0) { throw "La compilación falló" }

$apk = Join-Path $raiz "app\build\outputs\apk\debug\app-debug.apk"
Write-Host "==> APK generado: $apk ($([math]::Round((Get-Item $apk).Length / 1MB, 2)) MB)"
