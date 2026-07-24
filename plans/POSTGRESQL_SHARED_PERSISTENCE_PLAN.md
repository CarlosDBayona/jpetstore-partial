# Plan 2: Persistencia Compartida en PostgreSQL (Opción 2)
> **Proyecto Target:** [d:\jpetstore-partial](file:///D:/jpetstore-partial) & [d:\jpetstore-6](file:///D:/jpetstore-6)  
> **Objetivo:** Permitir que las peticiones originadas en la UI legacy (`jpetstore-6`) ejecuten su lógica en el código legacy pero persistan datos en PostgreSQL, compartiendo la base de datos con el nuevo proyecto Spring Boot (`jpetstore-partial`).

---

## 1. Arquitectura y Flujo de Datos

```
                               ┌────────────────────────────────────────────────────────┐
                               │                 Navegador del Usuario                  │
                               │                (Legacy JSP / UI Forms)                 │
                               └──────────────────────────┬─────────────────────────────┘
                                                          │ HTTP Requests (Form POST/GET)
                                                          ▼
                               ┌────────────────────────────────────────────────────────┐
                               │           d:\jpetstore-6 (Legacy App / WAR)            │
                               │  • ActionBean (Stripes UI Controllers)                 │
                               │  • Service Layer (@SpringBean OrderService, etc.)       │
                               │  • MyBatis Mappers (OrderMapper, AccountMapper, etc.)   │
                               │  • HikariCP Connection Pool -> PostgreSQL Driver       │
                               └──────────────────────────┬─────────────────────────────┘
                                                          │
                                                          │ JDBC (Port 5432)
                                                          ▼
                               ┌────────────────────────────────────────────────────────┐
                               │           PostgreSQL Database (Shared DB)              │
                               │           Database: jpetstore                          │
                               │           Tables: account, orders, product, sequence...│
                               └──────────────────────────▲─────────────────────────────┘
                                                          │
                                                          │ JDBC (Port 5432)
                                                          │
                               ┌──────────────────────────┴─────────────────────────────┐
                               │       d:\jpetstore-partial (Spring Boot App)           │
                               │  • Spring Boot 3.x / Java 17                             │
                               │  • RestControllers                                     │
                               │  • Reused MyBatis Mappers & Services                   │
                               └────────────────────────────────────────────────────────┘
```

---

## 2. Paso 1: Aprovisionamiento de PostgreSQL (Docker Compose)

Crea `d:\jpetstore-partial\docker-compose.yml` para desplegar la base de datos PostgreSQL localmente sin instalaciones manuales:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16-alpine
    container_name: jpetstore-postgres
    restart: always
    environment:
      POSTGRES_DB: jpetstore
      POSTGRES_USER: jpetstore
      POSTGRES_PASSWORD: jpetstore_pass
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

Comando para iniciar:
```bash
docker compose up -d
```

---

## 3. Paso 2: Configuración del Schema en PostgreSQL

### Script DDL (`schema-postgres.sql`)
Crea el esquema relacional en PostgreSQL. **Nota:** Conservar la tabla `sequence` permite mantener el generador de secuencias legacy `SequenceMapper` con cero cambios de código Java.

```sql
CREATE TABLE IF NOT EXISTS supplier (
    suppid INT NOT NULL,
    name VARCHAR(80) NULL,
    status VARCHAR(2) NOT NULL,
    addr1 VARCHAR(80) NULL,
    addr2 VARCHAR(80) NULL,
    city VARCHAR(80) NULL,
    state VARCHAR(80) NULL,
    zip VARCHAR(5) NULL,
    phone VARCHAR(80) NULL,
    CONSTRAINT pk_supplier PRIMARY KEY (suppid)
);

CREATE TABLE IF NOT EXISTS signon (
    username VARCHAR(25) NOT NULL,
    password VARCHAR(25) NOT NULL,
    CONSTRAINT pk_signon PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS account (
    userid VARCHAR(80) NOT NULL,
    email VARCHAR(80) NOT NULL,
    firstname VARCHAR(80) NOT NULL,
    lastname VARCHAR(80) NOT NULL,
    status VARCHAR(2) NULL,
    addr1 VARCHAR(80) NOT NULL,
    addr2 VARCHAR(40) NULL,
    city VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    zip VARCHAR(20) NOT NULL,
    country VARCHAR(20) NOT NULL,
    phone VARCHAR(80) NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS profile (
    userid VARCHAR(80) NOT NULL,
    langpref VARCHAR(80) NOT NULL,
    favcategory VARCHAR(30),
    mylistopt INT,
    banneropt INT,
    CONSTRAINT pk_profile PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS bannerdata (
    favcategory VARCHAR(80) NOT NULL,
    bannername VARCHAR(255) NULL,
    CONSTRAINT pk_bannerdata PRIMARY KEY (favcategory)
);

CREATE TABLE IF NOT EXISTS orders (
    orderid INT NOT NULL,
    userid VARCHAR(80) NOT NULL,
    orderdate DATE NOT NULL,
    shipaddr1 VARCHAR(80) NOT NULL,
    shipaddr2 VARCHAR(80) NULL,
    shipcity VARCHAR(80) NOT NULL,
    shipstate VARCHAR(80) NOT NULL,
    shipzip VARCHAR(20) NOT NULL,
    shipcountry VARCHAR(20) NOT NULL,
    billaddr1 VARCHAR(80) NOT NULL,
    billaddr2 VARCHAR(80) NULL,
    billcity VARCHAR(80) NOT NULL,
    billstate VARCHAR(80) NOT NULL,
    billzip VARCHAR(20) NOT NULL,
    billcountry VARCHAR(20) NOT NULL,
    courier VARCHAR(80) NOT NULL,
    totalprice DECIMAL(10,2) NOT NULL,
    billtofirstname VARCHAR(80) NOT NULL,
    billtolastname VARCHAR(80) NOT NULL,
    shiptofirstname VARCHAR(80) NOT NULL,
    shiptolastname VARCHAR(80) NOT NULL,
    creditcard VARCHAR(80) NOT NULL,
    exprdate VARCHAR(7) NOT NULL,
    cardtype VARCHAR(80) NOT NULL,
    locale VARCHAR(80) NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (orderid)
);

CREATE TABLE IF NOT EXISTS orderstatus (
    orderid INT NOT NULL,
    linenum INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(2) NOT NULL,
    CONSTRAINT pk_orderstatus PRIMARY KEY (orderid, linenum)
);

CREATE TABLE IF NOT EXISTS lineitem (
    orderid INT NOT NULL,
    linenum INT NOT NULL,
    itemid VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    unitprice DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_lineitem PRIMARY KEY (orderid, linenum)
);

CREATE TABLE IF NOT EXISTS category (
    catid VARCHAR(10) NOT NULL,
    name VARCHAR(80) NULL,
    descn VARCHAR(255) NULL,
    CONSTRAINT pk_category PRIMARY KEY (catid)
);

CREATE TABLE IF NOT EXISTS product (
    productid VARCHAR(10) NOT NULL,
    category VARCHAR(10) NOT NULL,
    name VARCHAR(80) NULL,
    descn VARCHAR(255) NULL,
    CONSTRAINT pk_product PRIMARY KEY (productid),
    CONSTRAINT fk_product_1 FOREIGN KEY (category) REFERENCES category (catid)
);

CREATE INDEX IF NOT EXISTS productCat ON product (category);
CREATE INDEX IF NOT EXISTS productName ON product (name);

CREATE TABLE IF NOT EXISTS item (
    itemid VARCHAR(10) NOT NULL,
    productid VARCHAR(10) NOT NULL,
    listprice DECIMAL(10,2) NULL,
    unitcost DECIMAL(10,2) NULL,
    supplier INT NULL,
    status VARCHAR(2) NULL,
    attr1 VARCHAR(80) NULL,
    attr2 VARCHAR(80) NULL,
    attr3 VARCHAR(80) NULL,
    attr4 VARCHAR(80) NULL,
    attr5 VARCHAR(80) NULL,
    CONSTRAINT pk_item PRIMARY KEY (itemid),
    CONSTRAINT fk_item_1 FOREIGN KEY (productid) REFERENCES product (productid),
    CONSTRAINT fk_item_2 FOREIGN KEY (supplier) REFERENCES supplier (suppid)
);

CREATE INDEX IF NOT EXISTS itemProd ON item (productid);

CREATE TABLE IF NOT EXISTS inventory (
    itemid VARCHAR(10) NOT NULL,
    qty INT NOT NULL,
    CONSTRAINT pk_inventory PRIMARY KEY (itemid)
);

CREATE TABLE IF NOT EXISTS sequence (
    name VARCHAR(30) NOT NULL,
    nextid INT NOT NULL,
    CONSTRAINT pk_sequence PRIMARY KEY (name)
);
```

---

## 4. Paso 3: Configuración de la App Legacy (`d:\jpetstore-6`)

### 1. Actualizar `pom.xml` en `d:\jpetstore-6`
Agregar el driver de PostgreSQL y HikariCP para el pool de conexiones:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.2</version>
</dependency>
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.1.0</version>
</dependency>
```

### 2. Modificar [applicationContext.xml](file:///D:/jpetstore-6/src/main/webapp/WEB-INF/applicationContext.xml)
Reemplazar la base de datos embebida HSQLDB (`<jdbc:embedded-database>`) por la conexión a PostgreSQL:

```xml
<!-- Reemplazar líneas 31-34 en applicationContext.xml -->
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
    <property name="driverClassName" value="org.postgresql.Driver" />
    <property name="jdbcUrl" value="jdbc:postgresql://localhost:5432/jpetstore" />
    <property name="username" value="jpetstore" />
    <property name="password" value="jpetstore_pass" />
    <property name="maximumPoolSize" value="10" />
</bean>
```

---

## 5. Paso 4: Configuración de Spring Boot (`d:\jpetstore-partial`)

### 1. Archivo `application.yml`
Configurar el origen de datos y mapeo MyBatis en `d:\jpetstore-partial\src\main\resources\application.yml`:

```yaml
spring:
  application:
    name: jpetstore-partial
  datasource:
    url: jdbc:postgresql://localhost:5432/jpetstore
    username: jpetstore
    password: jpetstore_pass
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
  sql:
    init:
      mode: always
      schema-locations: classpath:schema-postgres.sql
      data-locations: classpath:database/jpetstore-hsqldb-dataload.sql

mybatis:
  mapper-locations: classpath:org/mybatis/jpetstore/mapper/*.xml
  type-aliases-package: org.mybatis.jpetstore.domain
  configuration:
    map-underscore-to-camel-case: true
```

### 2. Mapper Scanning en Spring Boot
Garantizar que la aplicación principal escanee los mappers de MyBatis:

```java
package org.mybatis.jpetstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.mybatis.jpetstore.mapper")
public class JPetStorePartialApplication {
    public static void main(String[] args) {
        SpringApplication.run(JPetStorePartialApplication.class, args);
    }
}
```

---

## 6. Lista de Verificación y Protocolo de Ejecución

1. [ ] **Levantar PostgreSQL:** Ejecutar `docker compose up -d` en `d:\jpetstore-partial`.
2. [ ] **Inicializar Esquema:** Ejecutar `schema-postgres.sql` e insertar datos iniciales de `dataload`.
3. [ ] **Actualizar `jpetstore-6`:** Modificar `pom.xml` y `applicationContext.xml` con el `HikariDataSource` de PostgreSQL.
4. [ ] **Compilar y Ejecutar `jpetstore-6`:** Correr `./mvnw jetty:run` o desplegar el WAR en Tomcat.
5. [ ] **Verificación de Persistencia:**
   - Realizar una compra o crear un nuevo usuario desde la UI de `jpetstore-6`.
   - Consultar la base de datos PostgreSQL (`SELECT * FROM orders;` o `SELECT * FROM account;`).
   - Levantar `jpetstore-partial` y consultar el mismo registro vía la API REST Spring Boot para confirmar consistencia de datos compartidos.
