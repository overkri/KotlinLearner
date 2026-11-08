#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
printf "%-6s %-8s %-8s %-8s %-8s\n" "module" "tests" "passed" "failed" "skipped"

total_tests=0
total_passed=0
for module in $(seq -w 0 14); do
    tag="m${module}"
    tests=0; failures=0; errors=0; skipped=0
    while IFS= read -r file; do
        line=$(grep -o '<testsuite[^>]*>' "$file" | head -n 1)
        case "$line" in
            *"$tag"*) ;;
            *) continue ;;
        esac
        t=$(printf '%s' "$line" | sed -n 's/.*tests="\([0-9]*\)".*/\1/p')
        f=$(printf '%s' "$line" | sed -n 's/.*failures="\([0-9]*\)".*/\1/p')
        e=$(printf '%s' "$line" | sed -n 's/.*errors="\([0-9]*\)".*/\1/p')
        s=$(printf '%s' "$line" | sed -n 's/.*skipped="\([0-9]*\)".*/\1/p')
        tests=$((tests + ${t:-0})); failures=$((failures + ${f:-0})); errors=$((errors + ${e:-0})); skipped=$((skipped + ${s:-0}))
    done < <(find "$ROOT" -path '*/build/test-results/test/TEST-*.xml' -type f 2>/dev/null)

    passed=$((tests - failures - errors - skipped))
    total_tests=$((total_tests + tests)); total_passed=$((total_passed + passed))
    printf "%-6s %-8s %-8s %-8s %-8s\n" "$tag" "$tests" "$passed" "$((failures + errors))" "$skipped"
done

printf "TOTAL: %s/%s passed\n" "$total_passed" "$total_tests"
