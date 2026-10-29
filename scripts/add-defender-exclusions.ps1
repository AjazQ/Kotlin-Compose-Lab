# Requires Administrator — excludes Android/emulator/Gradle paths from Windows Defender.
$ErrorActionPreference = "Stop"

$paths = @(
    "$env:LOCALAPPDATA\Android\Sdk",
    "$env:USERPROFILE\.android",
    "$env:USERPROFILE\.gradle",
    "C:\Ajaz\RnD\android\Kotlin",
    "$env:TEMP\AndroidEmulator"
) + @(
    Get-ChildItem "$env:LOCALAPPDATA\Google" -Directory -Filter "AndroidStudio*" -ErrorAction SilentlyContinue |
        ForEach-Object { $_.FullName }
)

$paths = $paths | Where-Object { $_ } | Select-Object -Unique

foreach ($p in $paths) {
    Add-MpPreference -ExclusionPath $p
    Write-Host "Excluded: $p"
}

# Common emulator/process names (optional process exclusions)
$procs = @(
    "qemu-system-x86_64.exe",
    "emulator.exe",
    "adb.exe",
    "crashpad_handler.exe"
)
foreach ($proc in $procs) {
    try {
        Add-MpPreference -ExclusionProcess $proc
        Write-Host "Excluded process: $proc"
    } catch {
        Write-Host "Skip process $proc : $($_.Exception.Message)"
    }
}

Write-Host ""
Write-Host "=== Current ExclusionPath ==="
(Get-MpPreference).ExclusionPath
Write-Host "=== Current ExclusionProcess ==="
(Get-MpPreference).ExclusionProcess

"OK" | Out-File "$env:TEMP\defender-exclusions-ok.txt" -Encoding utf8
(Get-MpPreference).ExclusionPath | Out-File "$env:TEMP\defender-exclusions.txt" -Encoding utf8
