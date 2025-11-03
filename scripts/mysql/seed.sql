USE rrhhseguro;

-- Funcionarios iniciales
INSERT INTO funcionarios (
  tipo_identificacion, numero_identificacion, nombres, apellidos,
  estado_civil, sexo, direccion, telefono, fecha_nacimiento
) VALUES
('CC','1001001001','Ana Maria','Gomez Perez','Soltera','F','Calle 10 # 20-30','3001112233','1995-03-15'),
('CC','9002003004','Carlos Andres','Lopez Rios','Casado','M','Carrera 45 # 50-60','3012223344','1988-07-22'),
('TI','11445566','Luisa Fernanda','Rojas Quintero','Union Libre','F','Transversal 8 # 12-34','3023334455','1999-12-05');

-- Grupo familiar
INSERT INTO miembros_familia (funcionario_id, nombres, apellidos, parentesco, fecha_nacimiento, telefono) VALUES
-- Familia de Carlos (id 2)
(2,'Maria','Garcia','Esposa','1990-05-10','3009998888'),
(2,'Santiago','Lopez Garcia','Hijo','2015-09-12',NULL),
-- Familia de Ana (id 1)
(1,'Jorge','Gomez','Padre','1965-01-01','3101112222');

-- Formación académica
INSERT INTO formacion_academica (funcionario_id, universidad, nivel_estudio, titulo, ano_grado) VALUES
(1,'Universidad de Antioquia','Pregrado','Ingenieria de Sistemas',2018),
(1,'Universidad Nacional','Posgrado','Especializacion en Seguridad',2020),
(2,'EAFIT','Pregrado','Administracion de Empresas',2012);


