-- ================================================
-- Script de Migración: Renombrar ciudad->municipio y municipio->sector
-- Aplicación: Sistema de Asistencia
-- ================================================

-- Conectarse a la base de datos
-- \c asistenciadb

-- Renombrar la columna 'ciudad' a 'municipio' (temporalmente a 'municipio_temp' para evitar conflictos)
-- Primero renombramos 'municipio' a 'sector'
ALTER TABLE coordinadores RENAME COLUMN municipio TO sector;

-- Luego renombramos 'ciudad' a 'municipio'
ALTER TABLE coordinadores RENAME COLUMN ciudad TO municipio;

-- Verificar los cambios
-- \d coordinadores

-- ================================================
-- Nota: Si la tabla ya tiene datos, estos se preservarán automáticamente
-- ================================================

