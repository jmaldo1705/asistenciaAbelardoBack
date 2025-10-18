-- Insertar coordinadores de municipios de ejemplo
INSERT INTO coordinadores (municipio, nombre_completo, celular, confirmado, numero_invitados, observaciones, fecha_llamada) 
VALUES 
('Bogotá', 'María García López', '3101234567', false, 0, NULL, NULL),
('Medellín', 'Carlos Rodríguez Pérez', '3209876543', false, 0, NULL, NULL),
('Cali', 'Ana Martínez Torres', '3157894561', false, 0, NULL, NULL),
('Barranquilla', 'Juan Hernández Silva', '3168529637', false, 0, NULL, NULL),
('Cartagena', 'Laura Gómez Díaz', '3145678912', false, 0, NULL, NULL);

-- Ejemplo de coordinador confirmado con invitados
INSERT INTO coordinadores (municipio, nombre_completo, celular, confirmado, numero_invitados, observaciones, fecha_llamada) 
VALUES 
('Bucaramanga', 'Pedro Sánchez Castro', '3112345678', true, 3, 'Confirmó asistencia con 3 invitados', NOW());

-- Invitados para el coordinador de Bucaramanga (ID 6)
INSERT INTO invitados (nombre, cedula, telefono, coordinador_id) 
VALUES 
('María Rodríguez', '1098765432', '3201234567', 6),
('José García', '1087654321', '3159876543', 6),
('Carmen López', '1076543210', '3147896541', 6);
