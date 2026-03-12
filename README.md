# 🚴 VELOX STORE – Backend API

Backend del sistema **Velox Store**, una aplicación desarrollada con **Spring Boot** para la gestión de una tienda de bicicletas.

Este proyecto forma parte de un desarrollo académico del **SENA**, donde se implementa una **API REST** que permite gestionar la lógica de negocio del sistema y comunicarse con un **frontend desarrollado en Angular**.

---

# 📌 Descripción del Proyecto

El backend de **Velox Store** se encarga de:

* Gestionar la información de bicicletas
* Administrar clientes
* Registrar ventas
* Controlar inventario
* Gestionar pagos y facturación

La aplicación está desarrollada utilizando **Spring Boot** siguiendo una arquitectura por capas.

---

# 🛠️ Tecnologías Utilizadas

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* REST API

---

# 📂 Estructura del Proyecto

```text
backend
│
├── src
│   └── main
│       ├── java
│       │   └── com.sena.tienda
│       │       ├── controller   → Controladores REST
│       │       ├── dto          → Objetos de transferencia de datos
│       │       ├── model        → Entidades del sistema
│       │       ├── repository   → Interfaces de acceso a datos
│       │       ├── service      → Lógica de negocio
│       │       └── TiendaApplication
│       │
│       └── resources
│           └── application.properties
│
├── pom.xml
└── README.md
```

---

# 🚀 Cómo ejecutar el proyecto

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/VELOX-STORE-SENA-Backend.git
```

---

### 2️⃣ Abrir el proyecto

Abrir el proyecto en alguno de los siguientes IDE:

* IntelliJ IDEA
* Visual Studio Code
* Spring Tool Suite

---

### 3️⃣ Configurar la base de datos

Editar el archivo:

```text
src/main/resources/application.properties
```

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/velox_store
spring.datasource.username=root
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 4️⃣ Ejecutar la aplicación

Ejecutar la clase principal:

```
TiendaApplication
```

La API se ejecutará normalmente en:

```
http://localhost:8080
```

---

# 🌿 Flujo de trabajo con Git (GitFlow)

El proyecto utiliza una versión simplificada de **GitFlow**.

### Ramas principales

```
main
develop
feature/*
```

### Flujo de trabajo

```
feature → Pull Request → develop
develop → Pull Request → main
```

### Reglas del repositorio

❌ No hacer push directo a `main`
❌ No trabajar directamente en `develop`
✅ Crear siempre una rama `feature`

Ejemplo:

```
feature/bicicletas
feature/clientes
feature/ventas
```

---

# 👨‍💻 Equipo de Desarrollo

Proyecto desarrollado por aprendices del **SENA**.

Integrantes:

* Jovanny Castañeda Arias
* Jhojan Dario Porras
* Sebastian Ramirez
