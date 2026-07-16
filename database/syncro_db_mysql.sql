--
-- Script de Base de Datos para MySQL
-- Base de datos: syncro_db
--

CREATE DATABASE IF NOT EXISTS syncro_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE syncro_db;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `clientes`
-- --------------------------------------------------------

CREATE TABLE clientes (
    dni VARCHAR(20) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(120),
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `cuentas`
-- --------------------------------------------------------

CREATE TABLE cuentas (
    numero_cuenta VARCHAR(20) NOT NULL,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    dni_dueno VARCHAR(20) NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    notificaciones_sms BOOLEAN NOT NULL DEFAULT FALSE,
    moneda VARCHAR(20) NOT NULL DEFAULT 'SOLES',
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (numero_cuenta),
    FOREIGN KEY (dni_dueno) REFERENCES clientes(dni) ON DELETE CASCADE,
    CONSTRAINT chk_tipo_cuenta CHECK (tipo_cuenta IN ('Ahorros', 'Corriente'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `transacciones`
-- --------------------------------------------------------

CREATE TABLE transacciones (
    id INT NOT NULL AUTO_INCREMENT,
    tipo_operacion VARCHAR(20) NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    numero_cuenta VARCHAR(20) NOT NULL,
    numero_cuenta_destino VARCHAR(20) DEFAULT NULL,
    exitosa BOOLEAN NOT NULL DEFAULT TRUE,
    descripcion TEXT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (numero_cuenta) REFERENCES cuentas(numero_cuenta),
    FOREIGN KEY (numero_cuenta_destino) REFERENCES cuentas(numero_cuenta),
    CONSTRAINT chk_monto CHECK (monto > 0),
    CONSTRAINT chk_tipo_operacion CHECK (tipo_operacion IN ('DEPOSITO', 'RETIRO', 'TRANSFERENCIA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `auditoria`
-- --------------------------------------------------------

CREATE TABLE auditoria (
    id INT NOT NULL AUTO_INCREMENT,
    evento VARCHAR(50) NOT NULL,
    numero_cuenta VARCHAR(20) DEFAULT NULL,
    monto DECIMAL(15,2) DEFAULT NULL,
    descripcion TEXT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `notificaciones_sms`
-- --------------------------------------------------------

CREATE TABLE notificaciones_sms (
    id INT NOT NULL AUTO_INCREMENT,
    mensaje TEXT NOT NULL,
    enviado_a VARCHAR(30) DEFAULT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `configuracion_sistema`
-- --------------------------------------------------------

CREATE TABLE configuracion_sistema (
    id INT NOT NULL DEFAULT 1,
    sobregiro_habilitado BOOLEAN NOT NULL DEFAULT TRUE,
    seguro_anti_fraude BOOLEAN NOT NULL DEFAULT FALSE,
    sms_global_activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT chk_id_unico CHECK (id = 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar configuración por defecto
INSERT INTO configuracion_sistema (id, sobregiro_habilitado, seguro_anti_fraude, sms_global_activo) VALUES (1, TRUE, FALSE, TRUE);

-- --------------------------------------------------------
-- Vistas
-- --------------------------------------------------------

CREATE VIEW vista_capital_total AS
SELECT 
    COUNT(*) AS total_cuentas,
    SUM(saldo) AS capital_total,
    AVG(saldo) AS saldo_promedio,
    MAX(saldo) AS saldo_maximo,
    MIN(saldo) AS saldo_minimo
FROM cuentas
WHERE activa = TRUE;

CREATE VIEW vista_clientes_cuentas AS
SELECT 
    c.dni,
    c.nombre,
    c.telefono,
    ct.numero_cuenta,
    ct.tipo_cuenta,
    ct.saldo,
    ct.activa,
    ct.notificaciones_sms
FROM clientes c
LEFT JOIN cuentas ct ON c.dni = ct.dni_dueno
ORDER BY c.nombre;

CREATE VIEW vista_ultimas_transacciones AS
SELECT 
    t.id,
    t.tipo_operacion,
    t.monto,
    t.numero_cuenta,
    t.numero_cuenta_destino,
    t.exitosa,
    t.descripcion,
    t.fecha_hora,
    c.nombre AS nombre_cliente
FROM transacciones t
JOIN cuentas ct ON t.numero_cuenta = ct.numero_cuenta
JOIN clientes c ON ct.dni_dueno = c.dni
ORDER BY t.fecha_hora DESC
LIMIT 10;
