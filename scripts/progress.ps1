param(
    [string]$Root = ""
)

$ErrorActionPreference = "Stop"
if (-not $Root) {
    $Root = Split-Path -Parent $PSScriptRoot
}

$totalTests = 0
$totalPassed = 0
$rows = @()

foreach ($module in 0..14) {
    $tag = "m{0:D2}" -f $module
    $resultsDir = Get-ChildItem -Path (Join-Path $Root "*\build\test-results\test") -Directory -ErrorAction SilentlyContinue
    $tests = 0
    $failures = 0
    $errors = 0
    $skipped = 0
    foreach ($dir in $resultsDir) {
        Get-ChildItem -Path $dir.FullName -Filter "TEST-*.xml" -ErrorAction SilentlyContinue | ForEach-Object {
            [xml]$xml = Get-Content $_.FullName
            $suite = $xml.testsuite
            if ($null -ne $suite -and ($suite.name -like "*.$tag.*" -or $suite.name -like "*$tag*")) {
                $tests += [int]$suite.tests
                $failures += [int]$suite.failures
                $errors += [int]$suite.errors
                $skipped += [int]$suite.skipped
            }
        }
    }
    $passed = $tests - $failures - $errors - $skipped
    $totalTests += $tests
    $totalPassed += $passed
    $rows += [pscustomobject]@{ Module = $tag; Tests = $tests; Passed = $passed; Failed = $failures + $errors; Skipped = $skipped }
}

$rows | Format-Table -AutoSize
Write-Host ("TOTAL: {0}/{1} passed" -f $totalPassed, $totalTests)
