param(
    [string]$Module = "",
    [switch]$KeepOnFailure
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$temp = Join-Path ([System.IO.Path]::GetTempPath()) ("ledger-kt-verify-" + [guid]::NewGuid().ToString("N"))

try {
    New-Item -ItemType Directory -Path $temp | Out-Null

    # Copy the student workspace without build outputs and VCS metadata.
    $roboArgs = @($root, $temp, "/MIR", "/NFL", "/NDL", "/NJH", "/NJS", "/NP")
    foreach ($dir in @(".git", "build", ".gradle", ".kotlin", ".idea", "out")) {
        $roboArgs += @("/XD", (Join-Path $root $dir))
    }
    $roboArgs += @("/XF", "*.log")
    & robocopy @roboArgs | Out-Null
    if ($LASTEXITCODE -ge 8) { throw "robocopy failed with exit code $LASTEXITCODE" }

    # Overlay reference solutions on top of the stubs.
    & robocopy (Join-Path $root "solutions") $temp /E /NFL /NDL /NJH /NJS /NP | Out-Null
    if ($LASTEXITCODE -ge 8) { throw "solution overlay failed with exit code $LASTEXITCODE" }

    Push-Location $temp
    try {
        if ($Module) {
            & .\gradlew.bat test --rerun-tasks --no-build-cache "-Pmodule=$Module"
        } else {
            & .\gradlew.bat test --rerun-tasks --no-build-cache
        }
        $code = $LASTEXITCODE
    } finally {
        Pop-Location
    }

    if ($code -ne 0) {
        throw "Tests failed in $temp"
    }

    Write-Host "Solutions verified."
} catch {
    if ($KeepOnFailure) {
        Write-Host "Verification workspace kept at: $temp" -ForegroundColor Yellow
    }
    throw
} finally {
    if (-not $KeepOnFailure -and (Test-Path $temp)) {
        Remove-Item -Recurse -Force $temp -ErrorAction SilentlyContinue
    }
}
