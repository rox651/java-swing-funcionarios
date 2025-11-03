## RRHH Swing (MySQL) – CRUD de Funcionarios

Aplicación de escritorio en Java Swing con patrón DAO y manejo de excepciones para gestionar el CRUD de `funcionarios`. Incluye el modelo relacional con `miembros_familia` y `formacion_academica` (relaciones activas en la BD). Pensado para funcionar en macOS, Windows y Linux.

---

### Requisitos

-  Java 17+
-  Maven 3.9+
-  MySQL 8+

### Instalación de dependencias (por sistema)

macOS (Homebrew):

```bash
brew update
brew install temurin@17 maven mysql
brew services start mysql
```

Windows (winget):

```powershell
winget install EclipseAdoptium.Temurin.17.JDK
winget install Apache.Maven
winget install Oracle.MySQL
```

Windows (Chocolatey, alternativa):

```powershell
choco install temurin17 maven mysql --yes
```

Linux (Ubuntu/Debian):

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven mysql-server
sudo service mysql start
```

---

### 1) Crear base de datos y poblar datos

```bash
mysql -u root -p < scripts/mysql/schema.sql
mysql -u root -p < scripts/mysql/seed.sql
```

Crear un usuario de solo aplicación (recomendado):

```sql
CREATE USER 'rrhhapp'@'localhost' IDENTIFIED BY 'MiClaveSegura';
GRANT SELECT, INSERT, UPDATE, DELETE ON rrhhseguro.* TO 'rrhhapp'@'localhost';
FLUSH PRIVILEGES;
```

---

### 2) Configurar la conexión a MySQL

Edita `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/rrhhseguro?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
db.user=rrhhapp
db.password=MiClaveSegura
```

Notas:

-  En Windows, `allowPublicKeyRetrieval=true` suele evitar errores de clave pública.
-  Si tu MySQL no usa el puerto 3306 o está en otra máquina, ajusta `localhost:3306`.

---

### 3) Compilar y ejecutar

Compilar fat-jar (multiplataforma):

```bash
mvn clean package
```

Ejecutar la app:

```bash
java -jar target/rrhh-swing-1.0.0-jar-with-dependencies.jar
```

Ejecutar desde IDE (opcional):

-  Clase principal: `com.uidea.rrhh.Main`.
-  Asegúrate de que `db.properties` esté en el classpath (ya está bajo `src/main/resources`).

---

### Organización del proyecto

-  `src/main/java/com/uidea/rrhh/config`:
   -  `Config` lee `db.properties`.
   -  `Database` entrega `Connection` vía `DriverManager`.
-  `src/main/java/com/uidea/rrhh/model`: POJOs `Funcionario`, `MiembroFamilia`, `FormacionAcademica`.
-  `src/main/java/com/uidea/rrhh/dao`:
   -  `FuncionarioDAO` (interfaz) y `FuncionarioDAOImpl` (JDBC con `PreparedStatement`).
-  `src/main/java/com/uidea/rrhh/exception`:
   -  `DataAccessException`, `ValidationException`.
-  `src/main/java/com/uidea/rrhh/ui`:
   -  `FuncionarioFrame` (Swing) con CRUD, validaciones, y mensajes de error con causa.
-  `src/main/java/com/uidea/rrhh/Main`:
   -  Punto de entrada; configura Look&Feel del sistema y abre la UI.
-  `scripts/mysql`:
   -  `schema.sql` (creación BD/tablas/índices), `seed.sql` (datos de ejemplo).

---

### Modelo relacional (resumen)

-  `funcionarios` (PK `id`, `numero_identificacion` UNIQUE, `sexo` con `CHECK`).
-  `miembros_familia` (FK `funcionario_id` → `funcionarios.id`, ON DELETE CASCADE).
-  `formacion_academica` (FK `funcionario_id` → `funcionarios.id`, ON DELETE CASCADE).

---

### Seguridad aplicada

-  SQL seguro con `PreparedStatement` (sin concatenación de strings).
-  Restricciones de BD (`UNIQUE`, `CHECK`, `NOT NULL`, FKs con `CASCADE`).
-  Validación en UI (campos obligatorios y fecha `yyyy-MM-dd`).
-  Usuario de aplicación con permisos mínimos.

---

### Uso rápido de la app

1. Abrir la app.
2. En el formulario, completa: Tipo Ident. (combo), Num. Ident., Nombres, Apellidos, Estado Civil, Sexo, Dirección, Teléfono, Fecha Nac. (yyyy-MM-dd).
3. Guardar / Actualizar / Eliminar / Refrescar.

---

### Solución de problemas

-  Access denied (MySQL):
   -  Verifica `db.user` y `db.password`.
   -  Si usas usuario nuevo, ejecuta `FLUSH PRIVILEGES;`.
-  Public Key Retrieval not allowed (Windows):
   -  Agrega `allowPublicKeyRetrieval=true` en la URL.
-  No conecta al servidor:
   -  Asegura que MySQL esté iniciado (`brew services start mysql`, `service mysql start`, o Services en Windows).
-  Duplicado `numero_identificacion`:
   -  Cambia el valor; hay una restricción `UNIQUE`.
-  Fecha inválida:
   -  Usa formato `yyyy-MM-dd`.

---

### Comandos útiles

```bash
# Ver que el JAR contiene db.properties correcto
jar tf target/rrhh-swing-1.0.0-jar-with-dependencies.jar | grep db.properties

# Probar conexión MySQL con el usuario de app
mysql -u rrhhapp -p -e "SELECT 1;"
```
