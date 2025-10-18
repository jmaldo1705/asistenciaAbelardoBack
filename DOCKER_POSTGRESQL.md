# 🐳 PostgreSQL con Docker - Guía Rápida

## ⚡ Inicio Rápido (Opción más fácil)

Si tienes Docker Desktop instalado, estos comandos te permiten tener PostgreSQL funcionando en segundos:

### 1️⃣ Ejecutar PostgreSQL en Docker

```powershell
docker run --name postgres-asistencia `
  -e POSTGRES_PASSWORD=postgres `
  -e POSTGRES_DB=asistenciadb `
  -e POSTGRES_USER=postgres `
  -p 5432:5432 `
  -v postgres-asistencia-data:/var/lib/postgresql/data `
  -d postgres:16-alpine
```

**Explicación:**
- `--name`: Nombre del contenedor
- `-e POSTGRES_PASSWORD`: Contraseña del usuario postgres
- `-e POSTGRES_DB`: Nombre de la base de datos a crear
- `-e POSTGRES_USER`: Usuario de PostgreSQL
- `-p 5432:5432`: Mapea el puerto 5432 del contenedor al puerto 5432 del host
- `-v`: Crea un volumen para persistir los datos
- `-d`: Ejecuta en segundo plano
- `postgres:16-alpine`: Imagen de PostgreSQL 16 (versión ligera)

---

## 🎮 Comandos Esenciales

### Gestión del Contenedor

```powershell
# Ver contenedores en ejecución
docker ps

# Ver todos los contenedores (incluso detenidos)
docker ps -a

# Iniciar el contenedor
docker start postgres-asistencia

# Detener el contenedor
docker stop postgres-asistencia

# Reiniciar el contenedor
docker restart postgres-asistencia

# Eliminar el contenedor (¡cuidado! perderás los datos si no usaste volumen)
docker rm postgres-asistencia

# Ver logs en tiempo real
docker logs -f postgres-asistencia

# Ver los últimos 50 logs
docker logs --tail 50 postgres-asistencia
```

### Conectarse a PostgreSQL

```powershell
# Conectarse al contenedor con psql
docker exec -it postgres-asistencia psql -U postgres -d asistenciadb

# Ejecutar un comando SQL directamente
docker exec -it postgres-asistencia psql -U postgres -d asistenciadb -c "SELECT * FROM asistencias;"

# Abrir una terminal bash dentro del contenedor
docker exec -it postgres-asistencia bash
```

### Respaldos y Restauración

```powershell
# Crear un respaldo de la base de datos
docker exec -t postgres-asistencia pg_dump -U postgres asistenciadb > backup_asistencia_$(Get-Date -Format "yyyyMMdd_HHmmss").sql

# Restaurar desde un respaldo
Get-Content backup_asistencia.sql | docker exec -i postgres-asistencia psql -U postgres -d asistenciadb

# Crear respaldo comprimido
docker exec -t postgres-asistencia pg_dump -U postgres asistenciadb | gzip > backup_asistencia_$(Get-Date -Format "yyyyMMdd_HHmmss").sql.gz
```

---

## 🐳 Docker Compose (Recomendado para Desarrollo)

Crea un archivo `docker-compose.yml` en la raíz del proyecto:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: postgres-asistencia
    environment:
      POSTGRES_DB: asistenciadb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./database/create_database.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - asistencia-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  pgadmin:
    image: dpage/pgadmin4:latest
    container_name: pgadmin-asistencia
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@asistencia.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"
    volumes:
      - pgadmin-data:/var/lib/pgadmin
    networks:
      - asistencia-network
    restart: unless-stopped
    depends_on:
      - postgres

volumes:
  postgres-data:
  pgadmin-data:

networks:
  asistencia-network:
    driver: bridge
```

### Comandos de Docker Compose

```powershell
# Iniciar todos los servicios
docker-compose up -d

# Detener todos los servicios
docker-compose down

# Ver logs
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f postgres

# Reiniciar servicios
docker-compose restart

# Eliminar todo (incluyendo volúmenes)
docker-compose down -v
```

Con esta configuración también obtienes **pgAdmin** en: http://localhost:5050

---

## 🔧 Configuración de pgAdmin con Docker

Si usaste el docker-compose con pgAdmin:

1. Abre http://localhost:5050
2. Login:
   - Email: `admin@asistencia.com`
   - Password: `admin`

3. Agregar servidor:
   - Clic derecho en "Servers" → "Register" → "Server"
   - **General tab:**
     - Name: `Asistencia DB`
   - **Connection tab:**
     - Host: `postgres` (nombre del servicio en docker-compose)
     - Port: `5432`
     - Database: `asistenciadb`
     - Username: `postgres`
     - Password: `postgres`
   - Guardar

---

## 📊 Verificar el Estado

### Verificar que PostgreSQL está corriendo

```powershell
# Ver estado del contenedor
docker ps --filter "name=postgres-asistencia"

# Verificar conectividad
docker exec postgres-asistencia pg_isready -U postgres

# Ver bases de datos
docker exec -it postgres-asistencia psql -U postgres -c "\l"

# Ver tablas en asistenciadb
docker exec -it postgres-asistencia psql -U postgres -d asistenciadb -c "\dt"
```

---

## 🎯 Ventajas de usar Docker

✅ **Instalación rápida**: 1 comando y tienes PostgreSQL funcionando
✅ **Aislamiento**: No interfiere con tu sistema
✅ **Portabilidad**: Mismo entorno en cualquier máquina
✅ **Fácil limpieza**: Eliminar el contenedor y listo
✅ **Múltiples versiones**: Puedes tener varias versiones de PostgreSQL
✅ **Incluye pgAdmin**: Con docker-compose obtienes la interfaz gráfica

---

## 🚀 Iniciar tu Aplicación con Docker

### Opción 1: Solo PostgreSQL en Docker

```powershell
# 1. Iniciar PostgreSQL
docker start postgres-asistencia

# 2. Iniciar el backend (en otra terminal)
cd "c:\Users\Jonat\Documents\Asistencia\AsistenciaBack"
.\gradlew bootRun

# 3. Iniciar el frontend (en otra terminal)
cd "c:\Users\Jonat\Documents\Asistencia\AsistenciaFront"
npm start
```

### Opción 2: Todo con Docker Compose

```powershell
# Iniciar servicios
docker-compose up -d

# Iniciar backend
cd "c:\Users\Jonat\Documents\Asistencia\AsistenciaBack"
.\gradlew bootRun

# Iniciar frontend
cd "c:\Users\Jonat\Documents\Asistencia\AsistenciaFront"
npm start
```

---

## 🆘 Solución de Problemas

### Problema: Puerto 5432 ya está en uso

```powershell
# Ver qué proceso está usando el puerto
netstat -ano | findstr :5432

# Cambiar el puerto en docker-compose.yml
ports:
  - "5433:5432"  # Usar puerto 5433 en el host

# Actualizar application.properties
spring.datasource.url=jdbc:postgresql://localhost:5433/asistenciadb
```

### Problema: El contenedor no inicia

```powershell
# Ver los logs del contenedor
docker logs postgres-asistencia

# Verificar Docker Desktop
# Asegúrate de que Docker Desktop esté corriendo
```

### Problema: Error de conexión desde la aplicación

```powershell
# Verificar que el contenedor está corriendo
docker ps

# Verificar la red
docker network inspect bridge

# Probar conexión manualmente
docker exec -it postgres-asistencia psql -U postgres -d asistenciadb
```

---

## 📚 Recursos Adicionales

- **Documentación Docker PostgreSQL**: https://hub.docker.com/_/postgres
- **Docker Compose Documentation**: https://docs.docker.com/compose/
- **pgAdmin Documentation**: https://www.pgadmin.org/docs/

---

## 💡 Consejo

Para desarrollo, **Docker es la opción más rápida y limpia**. Para producción, considera instalar PostgreSQL directamente en el servidor.
