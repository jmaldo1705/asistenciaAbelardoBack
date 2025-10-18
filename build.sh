#!/bin/bash
# Script de build para Railway

echo "🚀 Iniciando build de Spring Boot..."

# Dar permisos de ejecución a gradlew
chmod +x ./gradlew

# Limpiar builds anteriores
echo "🧹 Limpiando builds anteriores..."
./gradlew clean --no-daemon

# Compilar el proyecto
echo "⚙️ Compilando el proyecto..."
./gradlew bootJar --no-daemon --info

# Verificar que el JAR se haya creado
if [ -f build/libs/asistencia-backend.jar ]; then
    echo "✅ JAR creado exitosamente: build/libs/asistencia-backend.jar"
    ls -lh build/libs/
else
    echo "❌ Error: No se pudo crear el JAR"
    exit 1
fi

echo "🎉 Build completado!"
