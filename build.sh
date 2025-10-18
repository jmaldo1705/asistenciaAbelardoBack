#!/bin/bash
# Script de build para Railway

set -e  # Salir si hay algún error

echo "🚀 Iniciando build de Spring Boot..."

# Mostrar versión de Java
echo "☕ Versión de Java:"
java -version

# Dar permisos de ejecución a gradlew
echo "🔐 Configurando permisos..."
chmod +x ./gradlew

# Limpiar builds anteriores
echo "🧹 Limpiando builds anteriores..."
./gradlew clean --no-daemon

# Compilar el proyecto (sin tests para acelerar)
echo "⚙️ Compilando el proyecto..."
./gradlew bootJar --no-daemon -x test --info

# Verificar que el JAR se haya creado
if [ -f build/libs/asistencia-backend.jar ]; then
    echo "✅ JAR creado exitosamente: build/libs/asistencia-backend.jar"
    ls -lh build/libs/
    echo "📦 Tamaño del JAR: $(du -h build/libs/asistencia-backend.jar | cut -f1)"
else
    echo "❌ Error: No se pudo crear el JAR"
    echo "📁 Contenido de build/libs:"
    ls -la build/libs/ || echo "Directorio build/libs no existe"
    exit 1
fi

echo "🎉 Build completado exitosamente!"
