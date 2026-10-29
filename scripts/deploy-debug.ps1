# Incremental deploy — compiles only what changed, then installs & launches.
# Prefer Android Studio Live Edit for true Compose hot reload while debugging.
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

$sdk = Join-Path $env:LOCALAPPDATA "Android\Sdk"
$env:PATH = "$sdk\platform-tools;$env:PATH"

Write-Host ">> Incremental compile + installDebug"
& .\gradlew.bat :app:installDebug
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host ">> Launch MainActivity"
adb shell am start -n com.ajaz.kotlinlab/.MainActivity
Write-Host "Done. For Compose Live Edit, Run (Debug) from Android Studio instead."
