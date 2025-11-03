-- Esquema de base de datos para RRHH Seguro (MySQL 8+)
-- Ejecutar con un usuario que tenga permisos de creación

DROP DATABASE IF EXISTS rrhhseguro;
CREATE DATABASE rrhhseguro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE rrhhseguro;

-- Tabla principal: funcionarios
CREATE TABLE funcionarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo_identificacion VARCHAR(10) NOT NULL,
  numero_identificacion VARCHAR(32) NOT NULL,
  nombres VARCHAR(120) NOT NULL,
  apellidos VARCHAR(120) NOT NULL,
  estado_civil VARCHAR(30) NOT NULL,
  sexo CHAR(1) NOT NULL,
  direccion VARCHAR(200) NULL,
  telefono VARCHAR(30) NULL,
  fecha_nacimiento DATE NOT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT uq_func_numid UNIQUE (numero_identificacion),
  CONSTRAINT ck_func_sexo CHECK (sexo IN ('M','F','O'))
);

-- Tabla: miembros de familia (grupo familiar)
CREATE TABLE miembros_familia (
  id INT AUTO_INCREMENT PRIMARY KEY,
  funcionario_id INT NOT NULL,
  nombres VARCHAR(120) NOT NULL,
  apellidos VARCHAR(120) NOT NULL,
  parentesco VARCHAR(60) NOT NULL,
  fecha_nacimiento DATE NULL,
  telefono VARCHAR(30) NULL,
  CONSTRAINT fk_mf_funcionario FOREIGN KEY (funcionario_id)
    REFERENCES funcionarios(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabla: formación académica
CREATE TABLE formacion_academica (
  id INT AUTO_INCREMENT PRIMARY KEY,
  funcionario_id INT NOT NULL,
  universidad VARCHAR(160) NOT NULL,
  nivel_estudio VARCHAR(80) NOT NULL,
  titulo VARCHAR(160) NOT NULL,
  ano_grado YEAR NULL,
  CONSTRAINT fk_fa_funcionario FOREIGN KEY (funcionario_id)
    REFERENCES funcionarios(id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE INDEX ix_func_numid ON funcionarios(numero_identificacion);
CREATE INDEX ix_mf_funcionario ON miembros_familia(funcionario_id);
CREATE INDEX ix_fa_funcionario ON formacion_academica(funcionario_id);


