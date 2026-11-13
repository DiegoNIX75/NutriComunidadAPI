# NutriComunidad API

API REST desarrollada con **Java** y **Spring Boot** para gestionar la información de NutriComunidad: comercios que donan alimentos, organizaciones que los reciben y distribuyen, beneficiarios, donaciones y usuarios del sistema.

## Tabla de contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Integrantes del equipo](#integrantes-del-equipo)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Módulos y endpoints](#módulos-y-endpoints)
- [Estructuras de datos](#estructuras-de-datos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Notas y limitaciones](#notas-y-limitaciones)

---

## Descripción

NutriComunidad API expone servicios para registrar y consultar a los actores que participan en la donación de alimentos:

- **Comercios** (`businesses`): fruterías, panaderías, supermercados, etc.
- **Organizaciones** (`organizations`): bancos de alimentos, comedores comunitarios, casas hogar.
- **Beneficiarios** (`beneficiaries`): personas y sus familias, con datos de contacto y domicilio.
- **Donaciones** (`donations`): fecha, cantidad, estado y observaciones de cada donación.
- **Usuarios** (`users`): cuentas de acceso al sistema.



## Tecnologías

| Tecnología | Versión / detalle |
|---|---|
| Java | 17 (toolchain de Gradle) |
| Spring Boot | 4.1.2 |
| Gradle | 9.8.2 (Gradle Wrapper incluido) |
| Spring Web MVC | `spring-boot-starter-webmvc` |
| Spring JDBC | `spring-boot-starter-jdbc` |
| PostgreSQL | Driver JDBC (`org.postgresql:postgresql`, en tiempo de ejecución) |
| JUnit Platform | Pruebas (`spring-boot-starter-test`) |

---

## Integrantes del equipo


| Autor        | Contacto     |
| ------------ | ------------ |
| Diego Alberto Juarez Angulo | diegoangulo967@gmail.com |
| Gael Alejandro Cerrillo Escanero | gace220106@gmail.com |


---

## Estructura del proyecto

```
src/main/java/com/example/apibasededatos/
├── ApiBaseDeDatosApplication.java   # Punto de entrada de Spring Boot
├── controllers/                     # Controladores REST (endpoints)
├── dto/                             # Objetos de petición (Create*Request)
├── models/                          # Modelos de datos (records)
├── services/                        # Lógica de negocio y almacenamiento en memoria
└── structures/                      # Estructuras de datos propias
src/main/resources/
└── application.properties           # Configuración de la aplicación
```

---

## Módulos y endpoints

**URL base local:** `http://localhost:8080`. El puerto no está configurado en `application.properties`, así que se usa el valor por defecto de Spring Boot.



### Health

Verifica que la API esté en ejecución y que exista conexión con la base de datos.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/health` | Devuelve `OK` si la API está activa. |
| GET | `/api/health/db` | Ejecuta `SELECT 1` mediante `JdbcTemplate` y devuelve `Database OK` si hay conexión con la base de datos. |

### Beneficiaries

Gestiona a los beneficiarios: datos personales, número de integrantes de la familia y domicilio.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/beneficiaries` | Lista todos los beneficiarios. |
| GET | `/api/beneficiaries/{id}` | Obtiene un beneficiario por su `id`. |
| POST | `/api/beneficiaries` | Crea un beneficiario. |
| PUT | `/api/beneficiaries/{id}` | Actualiza un beneficiario existente. |
| DELETE | `/api/beneficiaries/{id}` | Elimina un beneficiario. |

<details>
<summary>Ejemplo de cuerpo (POST / PUT)</summary>

```json
{
  "name": "Carlos",
  "lastName": "Hernandez Lopez",
  "phoneNumber": "2223456789",
  "mail": "carlos.hernandez@example.com",
  "familyMembers": 4,
  "street": "Avenida Reforma",
  "exteriorNumber": "125",
  "interiorNumber": "2B",
  "neighborhood": "Centro",
  "postalCode": "72000",
  "municipality": "...",
  "state": "..."
}
```
</details>

### Businesses

Gestiona los comercios donantes: nombre comercial, razón social, giro, contacto y domicilio.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/businesses` | Lista todos los comercios. |
| GET | `/api/businesses/{id}` | Obtiene un comercio por su `id`. |
| POST | `/api/businesses` | Crea un comercio. |
| PUT | `/api/businesses/{id}` | Actualiza un comercio existente. |
| DELETE | `/api/businesses/{id}` | Elimina un comercio. |

<details>
<summary>Ejemplo de cuerpo (POST / PUT)</summary>

```json
{
  "businessName": "Abarrotes La Esperanza",
  "legalName": "Comercializadora La Esperanza S.A. de C.V.",
  "typeOfBusiness": "Grocery Store",
  "phoneNumber": "2224567890",
  "mail": "contacto@laesperanza.example.com",
  "street": "Avenida Juarez",
  "exteriorNumber": "245",
  "interiorNumber": "...",
  "neighborhood": "...",
  "postalCode": "...",
  "municipality": "...",
  "state": "..."
}
```
</details>

### Donations

Gestiona las donaciones. Además del CRUD, permite consultar las donaciones eliminadas y restaurar la última eliminada.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/donations` | Lista todas las donaciones activas. |
| GET | `/api/donations/{id}` | Obtiene una donación por su `id`. |
| POST | `/api/donations` | Crea una donación. |
| PUT | `/api/donations/{id}` | Actualiza una donación existente. |
| DELETE | `/api/donations/{id}` | Elimina una donación y la guarda en la pila de eliminadas. |
| GET | `/api/donations/deleted` | Lista las donaciones eliminadas, de la más reciente a la más antigua. |
| POST | `/api/donations/restore` | Restaura la última donación eliminada. Devuelve `404` si no hay ninguna. |

<details>
<summary>Ejemplo de cuerpo (POST / PUT)</summary>

```json
{
  "donationDate": "2026-11-12",
  "totalQuantity": 25.5,
  "status": "PENDING",
  "observations": "Donation of fruits, vegetables, rice and beans."
}
```
</details>

### Organizations

Gestiona las organizaciones receptoras: nombre, tipo, responsable, contacto y domicilio.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/organizations` | Lista todas las organizaciones. |
| GET | `/api/organizations/{id}` | Obtiene una organización por su `id`. |
| POST | `/api/organizations` | Crea una organización. |
| PUT | `/api/organizations/{id}` | Actualiza una organización existente. |
| DELETE | `/api/organizations/{id}` | Elimina una organización. |

<details>
<summary>Ejemplo de cuerpo (POST / PUT)</summary>

```json
{
  "nameORG": "Fundacion Manos Unidas",
  "typeOfORG": "Non-Profit Organization",
  "manager": "Laura Martinez Garcia",
  "phoneNumber": "2225678901",
  "mail": "contacto@manosunidas.example.com",
  "street": "Calle 5 Sur",
  "exteriorNumber": "318",
  "interiorNumber": "N/A",
  "neighborhood": "...",
  "postalCode": "...",
  "municipality": "...",
  "state": "..."
}
```
</details>

### Users

Gestiona las cuentas de usuario del sistema.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/users` | Lista todos los usuarios. |
| GET | `/api/users/{id}` | Obtiene un usuario por su `id`. |
| POST | `/api/users` | Crea un usuario. |
| PUT | `/api/users/{id}` | Actualiza un usuario existente. |
| DELETE | `/api/users/{id}` | Elimina un usuario. |

<details>
<summary>Ejemplo de cuerpo (POST / PUT)</summary>

```json
{
  "name": "admin",
  "mail": "admin@nutricomunidad.example.com",
  "password": "Demo1234!Secure"
}
```
</details>

---

## Estructuras de datos

La API todavía no usa una base de datos persistente para sus recursos. Cada **service** (`@Service`) mantiene los registros en memoria mediante estructuras de datos implementadas en el paquete `structures`.

Los **controllers** no acceden a estas estructuras directamente. Reciben la petición y delegan la operación en el service correspondiente.

### Estructuras de almacenamiento

| Estructura | Tipo | Dónde se usa | Propósito | Operaciones |
|---|---|---|---|---|
| `SinglyLinkedList<T>` | Lista simplemente enlazada (genérica) | `UsersService`, `BeneficiariesService`, `BusinessesService`, `OrganizationsService`, `DonationsService` | Simula la "tabla" de cada recurso. Guarda los registros en orden de inserción. | `add`, `find`, `replace`, `remove`, `toList`, `size`, `isEmpty` |
| `LinkedStack<T>` | Pila enlazada (LIFO, genérica) | `DonationsService` (`pilaEliminadas`) | Guarda las donaciones eliminadas para poder consultarlas y restaurar la última. | `push`, `pop`, `peek`, `toList`, `size`, `isEmpty` |
| `Node<T>` | Nodo enlazado (auxiliar, visibilidad de paquete) | Interno a `SinglyLinkedList` y `LinkedStack` | Almacena un valor y la referencia al siguiente nodo. | — |

### Modelos de datos

Los modelos y DTOs **no son estructuras de almacenamiento**. Definen la forma de los datos que se guardan dentro de las estructuras anteriores y que se intercambian en las peticiones.

| Tipo | Clases | Uso |
|---|---|---|
| Modelos (`record`) | `users`, `beneficiaries`, `businesses`, `organizations`, `donations` | Representan cada registro almacenado. Incluyen el campo `id`. |
| DTOs (`record`) | `CreateUserRequest`, `CreateBeneficiaryRequest`, `CreateBusinessRequest`, `CreateOrganizationRequest`, `CreateDonationRequest` | Cuerpo de las peticiones `POST` y `PUT`. No incluyen `id`. |

---

## Instalación y ejecución

### Requisitos previos

- **JDK 17.**
- **Git.**
- **PostgreSQL** accesible, para que el endpoint `/api/health/db` funcione. El proyecto incluye el driver de PostgreSQL y lee la conexión desde variables de entorno.
- No es necesario instalar Gradle, porque el proyecto incluye el Gradle Wrapper.

### 1. Clonar el repositorio

```bash
git clone https://github.com/DiegoNIX75/NutriComunidadAPI.git
cd NutriComunidadAPI
```

### 2. Configurar las variables de entorno

`application.properties` importa un archivo `.env` opcional desde la raíz del proyecto (`spring.config.import=optional:file:.env[.properties]`) y usa estas variables:

| Variable | Uso |
|---|---|
| `DB_URL` | URL JDBC de la base de datos (`spring.datasource.url`). |
| `DB_USER` | Usuario de la base de datos (`spring.datasource.username`). |
| `DB_PASSWORD` | Contraseña de la base de datos (`spring.datasource.password`). |

Pasos:

1. Crea un archivo `.env` en la raíz del proyecto. Ya está incluido en `.gitignore`.
2. Define en él las tres variables con los valores de tu instancia de PostgreSQL, en formato `CLAVE=valor`.



### 3. Ejecutar el proyecto

En Linux o macOS:

```bash
./gradlew bootRun
```

En Windows:

```bash
gradlew.bat bootRun
```

Este comando compila el proyecto e inicia la aplicación Spring Boot mediante Gradle.

### 4. Verificar el funcionamiento

```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/api/health/db
curl http://localhost:8080/api/beneficiaries
```

### Pruebas con Postman

El equipo cuenta con la colección de Postman **AlimentacionSaludable**. Para usarla:

1. Asigna a la variable `{{url}}` el valor `http://localhost:8080/api`.
2. Ten en cuenta que la colección no incluye `GET /api/health/db`, `GET /api/donations/deleted` ni `POST /api/donations/restore`.

---
