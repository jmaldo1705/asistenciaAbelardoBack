-- ================================================
-- Script de Creación de Base de Datos PostgreSQL
-- Aplicación: Sistema de Asistencia
-- ================================================

-- Crear la base de datos (ejecutar como usuario postgres)
CREATE DATABASE asistenciadb
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Spain.1252'
    LC_CTYPE = 'Spanish_Spain.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conectarse a la base de datos
\c asistenciadb

-- Crear la tabla asistencias (opcional - Hibernate lo hace automáticamente)
-- Solo usar si prefieres crear la tabla manualmente

CREATE TABLE IF NOT EXISTS public.asistencias
(
    id bigserial NOT NULL,
    nombre character varying(255) NOT NULL,
    apellido character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    fecha_hora timestamp without time zone NOT NULL,
    observaciones character varying(500),
    presente boolean NOT NULL DEFAULT true,
    CONSTRAINT asistencias_pkey PRIMARY KEY (id)
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_asistencias_nombre ON public.asistencias(nombre);
CREATE INDEX IF NOT EXISTS idx_asistencias_apellido ON public.asistencias(apellido);
CREATE INDEX IF NOT EXISTS idx_asistencias_email ON public.asistencias(email);
CREATE INDEX IF NOT EXISTS idx_asistencias_fecha_hora ON public.asistencias(fecha_hora DESC);
CREATE INDEX IF NOT EXISTS idx_asistencias_presente ON public.asistencias(presente);

-- Agregar comentarios a la tabla y columnas
COMMENT ON TABLE public.asistencias IS 'Tabla para almacenar registros de asistencia';
COMMENT ON COLUMN public.asistencias.id IS 'Identificador único de la asistencia';
COMMENT ON COLUMN public.asistencias.nombre IS 'Nombre de la persona';
COMMENT ON COLUMN public.asistencias.apellido IS 'Apellido de la persona';
COMMENT ON COLUMN public.asistencias.email IS 'Correo electrónico de la persona';
COMMENT ON COLUMN public.asistencias.fecha_hora IS 'Fecha y hora del registro';
COMMENT ON COLUMN public.asistencias.observaciones IS 'Observaciones adicionales';
COMMENT ON COLUMN public.asistencias.presente IS 'Indica si la persona estuvo presente';

-- Insertar datos de prueba (opcional)
INSERT INTO public.asistencias (nombre, apellido, email, fecha_hora, observaciones, presente)
VALUES 
    ('Juan', 'Pérez', 'juan.perez@email.com', NOW(), 'Llegó puntual', true),
    ('María', 'González', 'maria.gonzalez@email.com', NOW(), 'Primera asistencia', true),
    ('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', NOW(), NULL, true),
    ('Ana', 'López', 'ana.lopez@email.com', NOW(), 'Justificó su ausencia', false);

-- Consultar los datos insertados
SELECT * FROM public.asistencias ORDER BY fecha_hora DESC;

-- ================================================
-- Comandos útiles de PostgreSQL
-- ================================================

-- Ver todas las tablas
-- \dt

-- Describir la estructura de una tabla
-- \d asistencias

-- Ver todas las bases de datos
-- \l

-- Cambiar de base de datos
-- \c nombre_base_datos

-- Salir de psql
-- \q

-- Contar registros
-- SELECT COUNT(*) FROM asistencias;

-- Ver los últimos 10 registros
-- SELECT * FROM asistencias ORDER BY fecha_hora DESC LIMIT 10;

-- Buscar por nombre
-- SELECT * FROM asistencias WHERE nombre ILIKE '%juan%';

-- Estadísticas de asistencia
-- SELECT 
--     presente,
--     COUNT(*) as total,
--     ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) as porcentaje
-- FROM asistencias
-- GROUP BY presente;
