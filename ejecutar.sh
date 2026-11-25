#!/usr/bin/env bash
# Compila y ejecuta la aplicación de consola.
set -e
mkdir -p out/main
javac -encoding UTF-8 -d out/main $(find src -name '*.java')
java -cp out/main edu.ups.poo.audiovisual.MainAudiovisual "$@"
