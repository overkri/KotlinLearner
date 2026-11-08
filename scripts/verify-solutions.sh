#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MODULE="${1:-}"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

if command -v rsync >/dev/null 2>&1; then
    rsync -a \
        --exclude '.git' --exclude 'build' --exclude '.gradle' \
        --exclude '.kotlin' --exclude '.idea' --exclude 'out' \
        "$ROOT/" "$TMP/"
else
    tar -C "$ROOT" \
        --exclude='.git' --exclude='build' --exclude='.gradle' \
        --exclude='.kotlin' --exclude='.idea' --exclude='out' \
        -cf - . | tar -C "$TMP" -xf -
fi

cp -R "$ROOT/solutions/." "$TMP/"

cd "$TMP"
if [[ -n "$MODULE" ]]; then
    ./gradlew test "-Pmodule=$MODULE"
else
    ./gradlew test
fi
