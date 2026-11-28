-- =====================================================================
-- NutriComunidad - Esquema PostgreSQL (Neon)
-- Basado en el diccionario de datos del proyecto.
-- Ajustes respecto al diccionario (ver README, sección "Decisiones"):
--   * DETALLE_DONACION lleva id_donacion y DETALLE_RECETAS lleva id_receta
--     (relación 1:N); DONACIONES y RECETAS ya no apuntan a un único detalle.
--   * ALERGIAS usa llave primaria compuesta (id_preferencias, id_alimento, id_beneficiario).
--   * contrasena_usuario almacena únicamente el hash BCrypt.
--   * BENEFICIARIOS, COMERCIOS y ORGANIZACIONES tienen id_usuario (cuenta propietaria).
-- El script es idempotente: puede ejecutarse varias veces.
-- =====================================================================

CREATE TABLE IF NOT EXISTS roles (
    id_rol        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_rol    VARCHAR(50)  NOT NULL UNIQUE,
    descripcion   VARCHAR(255),
    estado        VARCHAR(20)  NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_usuario      VARCHAR(100) NOT NULL,
    ap_usuario          VARCHAR(100) NOT NULL,
    am_usuario          VARCHAR(100),
    correo_usuario      VARCHAR(150) NOT NULL UNIQUE,
    contrasena_usuario  VARCHAR(100) NOT NULL,
    telefono_usuario    VARCHAR(20),
    fecha_registro      DATE         NOT NULL DEFAULT CURRENT_DATE,
    estado              VARCHAR(20)  NOT NULL DEFAULT 'Activo',
    id_rol              INTEGER      NOT NULL REFERENCES roles (id_rol)
);

CREATE TABLE IF NOT EXISTS beneficiarios (
    id_beneficiario      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_beneficiario  VARCHAR(100) NOT NULL,
    ap                   VARCHAR(100) NOT NULL,
    am                   VARCHAR(100),
    telefono             VARCHAR(20),
    correo               VARCHAR(150),
    numero_integrantes   INTEGER      CHECK (numero_integrantes > 0),
    calle                VARCHAR(150),
    numero_exterior      VARCHAR(20),
    numero_interior      VARCHAR(20),
    colonia              VARCHAR(100),
    codigo_postal        VARCHAR(10),
    municipio            VARCHAR(100),
    estado_beneficiario  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS organizaciones (
    id_organizacion               INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_organizacion           VARCHAR(150) NOT NULL,
    tipo_organizacion             VARCHAR(100),
    responsable_organizacion      VARCHAR(150),
    telefono_organizacion         VARCHAR(20),
    correo_organizacion           VARCHAR(150),
    calle_organizacion            VARCHAR(150),
    numero_exterior_organizacion  VARCHAR(20),
    numero_interior_organizacion  VARCHAR(20),
    colonia_organizacion          VARCHAR(100),
    codigo_postal_organizacion    VARCHAR(10),
    municipio_organizacion        VARCHAR(100),
    estado_organizacion           VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS comercios (
    id_comercio               INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_comercio           VARCHAR(150) NOT NULL,
    razon_social              VARCHAR(200),
    tipo_comercio             VARCHAR(100),
    telefono_comercio         VARCHAR(20),
    correo_comercio           VARCHAR(150),
    calle_comercio            VARCHAR(150),
    numero_exterior_comercio  VARCHAR(20),
    numero_interior_comercio  VARCHAR(20),
    colonia_comercio          VARCHAR(100),
    codigo_postal_comercio    VARCHAR(10),
    municipio_comercio        VARCHAR(100),
    estado_comercio           VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS categorias_alimentos (
    id_categoria           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_categoria       VARCHAR(100) NOT NULL UNIQUE,
    descripcion_categoria  VARCHAR(255),
    estado_categoria       VARCHAR(20)  NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS alimentos (
    id_alimento           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_alimento       VARCHAR(100)  NOT NULL,
    descripcion           VARCHAR(255),
    unidad_medida         VARCHAR(20),
    calorias_por_porcion  NUMERIC(10,2) CHECK (calorias_por_porcion >= 0),
    proteinas             NUMERIC(10,2) CHECK (proteinas >= 0),
    carbohidratos         NUMERIC(10,2) CHECK (carbohidratos >= 0),
    grasas                NUMERIC(10,2) CHECK (grasas >= 0),
    estado_alimento       VARCHAR(20)   NOT NULL DEFAULT 'Activo',
    id_categoria          INTEGER       NOT NULL REFERENCES categorias_alimentos (id_categoria)
);

CREATE TABLE IF NOT EXISTS preferencias (
    id_preferencias            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_preferencia           VARCHAR(100) NOT NULL,
    descripcion_preferencia    VARCHAR(255),
    observaciones_preferencia  VARCHAR(255),
    id_beneficiario            INTEGER      NOT NULL REFERENCES beneficiarios (id_beneficiario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS alergias (
    id_preferencias  INTEGER NOT NULL REFERENCES preferencias (id_preferencias) ON DELETE CASCADE,
    id_alimento      INTEGER NOT NULL REFERENCES alimentos (id_alimento),
    id_beneficiario  INTEGER NOT NULL REFERENCES beneficiarios (id_beneficiario) ON DELETE CASCADE,
    PRIMARY KEY (id_preferencias, id_alimento, id_beneficiario)
);

CREATE TABLE IF NOT EXISTS solicitudes (
    id_solicitud        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_solicitud     DATE         NOT NULL DEFAULT CURRENT_DATE,
    motivo              VARCHAR(255) NOT NULL,
    numero_integrantes  INTEGER      CHECK (numero_integrantes > 0),
    prioridad           VARCHAR(20),
    estado_solicitud    VARCHAR(30),
    id_organizacion     INTEGER      NOT NULL REFERENCES organizaciones (id_organizacion),
    id_beneficiario     INTEGER      NOT NULL REFERENCES beneficiarios (id_beneficiario)
);

CREATE TABLE IF NOT EXISTS entregas (
    id_entrega     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_entrega  DATE         NOT NULL,
    responsable    VARCHAR(150) NOT NULL,
    estado         VARCHAR(30),
    observaciones  VARCHAR(255),
    id_solicitud   INTEGER      NOT NULL REFERENCES solicitudes (id_solicitud)
);

CREATE TABLE IF NOT EXISTS donaciones (
    id_donacion             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_donacion          DATE          NOT NULL DEFAULT CURRENT_DATE,
    cantidad_total          NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (cantidad_total >= 0),
    estado_donacion         VARCHAR(30),
    observaciones_donacion  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS detalle_donacion (
    id_detalle_donacion             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cantidad_detalle_donacion       NUMERIC(12,2) NOT NULL CHECK (cantidad_detalle_donacion > 0),
    unidad_medida_detalle_donacion  VARCHAR(20),
    id_alimento                     INTEGER       NOT NULL REFERENCES alimentos (id_alimento),
    id_comercio                     INTEGER       NOT NULL REFERENCES comercios (id_comercio),
    id_donacion                     INTEGER       NOT NULL REFERENCES donaciones (id_donacion) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS inventario (
    id_inventario        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cantidad_disponible  NUMERIC(12,2) NOT NULL CHECK (cantidad_disponible >= 0),
    fecha_actualizacion  DATE          NOT NULL DEFAULT CURRENT_DATE,
    estado_inventario    VARCHAR(30),
    id_donacion          INTEGER       NOT NULL REFERENCES donaciones (id_donacion),
    id_organizacion      INTEGER       NOT NULL REFERENCES organizaciones (id_organizacion)
);

CREATE TABLE IF NOT EXISTS lotes (
    id_lote           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_lote       VARCHAR(50)   NOT NULL,
    fecha_recepcion   DATE          NOT NULL,
    fecha_caducidad   DATE,
    fecha_consumo     DATE,
    cantidad_inicial  NUMERIC(12,2) NOT NULL CHECK (cantidad_inicial > 0),
    cantidad_actual   NUMERIC(12,2) NOT NULL CHECK (cantidad_actual >= 0),
    id_inventario     INTEGER       NOT NULL REFERENCES inventario (id_inventario),
    CHECK (cantidad_actual <= cantidad_inicial),
    CHECK (fecha_caducidad IS NULL OR fecha_caducidad >= fecha_recepcion)
);

CREATE TABLE IF NOT EXISTS recetas (
    id_receta           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_receta       VARCHAR(150)  NOT NULL,
    descripcion         TEXT,
    tiempo_preparacion  VARCHAR(50),
    proporciones        VARCHAR(50),
    costo_estimado      NUMERIC(10,2) CHECK (costo_estimado >= 0)
);

CREATE TABLE IF NOT EXISTS detalle_recetas (
    id_detalle_receta  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cantidad           NUMERIC(10,2) NOT NULL CHECK (cantidad > 0),
    unidad_medida      VARCHAR(20),
    id_alimento        INTEGER       NOT NULL REFERENCES alimentos (id_alimento),
    id_receta          INTEGER       NOT NULL REFERENCES recetas (id_receta) ON DELETE CASCADE
);

-- Vínculo de cada cuenta con su beneficiario, comercio u organización (1:1, opcional).
-- ADD COLUMN IF NOT EXISTS permite aplicar este script también sobre una base ya creada.
ALTER TABLE beneficiarios  ADD COLUMN IF NOT EXISTS id_usuario INTEGER UNIQUE REFERENCES usuarios (id_usuario) ON DELETE SET NULL;
ALTER TABLE comercios      ADD COLUMN IF NOT EXISTS id_usuario INTEGER UNIQUE REFERENCES usuarios (id_usuario) ON DELETE SET NULL;
ALTER TABLE organizaciones ADD COLUMN IF NOT EXISTS id_usuario INTEGER UNIQUE REFERENCES usuarios (id_usuario) ON DELETE SET NULL;

-- Índices para las llaves foráneas más consultadas
CREATE INDEX IF NOT EXISTS idx_usuarios_rol             ON usuarios (id_rol);
CREATE INDEX IF NOT EXISTS idx_alimentos_categoria      ON alimentos (id_categoria);
CREATE INDEX IF NOT EXISTS idx_preferencias_benef       ON preferencias (id_beneficiario);
CREATE INDEX IF NOT EXISTS idx_solicitudes_benef        ON solicitudes (id_beneficiario);
CREATE INDEX IF NOT EXISTS idx_solicitudes_org          ON solicitudes (id_organizacion);
CREATE INDEX IF NOT EXISTS idx_entregas_solicitud       ON entregas (id_solicitud);
CREATE INDEX IF NOT EXISTS idx_detalle_donacion_don     ON detalle_donacion (id_donacion);
CREATE INDEX IF NOT EXISTS idx_inventario_org           ON inventario (id_organizacion);
CREATE INDEX IF NOT EXISTS idx_lotes_inventario         ON lotes (id_inventario);
CREATE INDEX IF NOT EXISTS idx_detalle_recetas_receta   ON detalle_recetas (id_receta);

-- Roles del sistema (los nombres deben coincidir con com.nutricomunidad.security.Roles)
INSERT INTO roles (nombre_rol, descripcion) VALUES
    ('Administrador', 'Administra usuarios, roles y catálogos de la plataforma'),
    ('Beneficiario',  'Persona o familia que solicita apoyo alimentario'),
    ('Comercio',      'Establecimiento que dona alimentos'),
    ('Organizacion',  'Organización que gestiona solicitudes, entregas e inventario')
ON CONFLICT (nombre_rol) DO NOTHING;
