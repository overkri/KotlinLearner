param(
    [Parameter(Mandatory = $true)]
    [string]$Module
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
Push-Location $root
try {
    & .\gradlew.bat test "-Pmodule=$Module"
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
