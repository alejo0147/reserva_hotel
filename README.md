# Proyecto de Reserva de Hotel

## Descripción

Este proyecto es un sistema de reservas de hotel que permite gestionar reservas asociadas a usuarios y servicios. El sistema está construido utilizando Java con Spring Boot y proporciona una API RESTful para manejar las operaciones de reserva. Además, se ha dockerizado la aplicación para simplificar su despliegue y manejo.

## Funcionalidades

- **Gestión de Reservas**: Crear, actualizar, eliminar y listar reservas.
- **Asociación con Usuarios y Servicios**: Cada reserva está vinculada a un usuario y a un servicio. Al consultar una reserva por su ID, se obtienen también los detalles del usuario y del servicio asociados.
- **Validación de Fechas**: Se realiza una validación para asegurar que la fecha de finalización de una reserva no sea anterior a la fecha de inicio.

## Endpoints Disponibles

### Reservas

- **GET /reservations**: Obtiene una lista de todas las reservas.
- **GET /reservations/{id}**: Obtiene los detalles de una reserva específica, incluyendo los datos del usuario y del servicio asociados.
- **POST /reservations**: Crea una nueva reserva.
- **PUT /reservations/{id}**: Actualiza una reserva existente.
- **DELETE /reservations/{id}**: Elimina una reserva específica.
- **GET /reservations/date-range/{startDate}**: Obtiene reservas en un rango de fechas a partir de una fecha de inicio.
- **GET /reservations/user/{userId}**: Obtiene reservas asociadas a un usuario específico.
- **GET /reservations/service/{serviceId}**: Obtiene reservas asociadas a un servicio específico.

### Servicios

- **GET /services**: Obtiene una lista de todos los servicios disponibles.
- **GET /services/{id}**: Obtiene los detalles de un servicio específico.
- **POST /services**: Crea un nuevo servicio.
- **PUT /services/{id}**: Actualiza un servicio existente.
- **DELETE /services/{id}**: Elimina un servicio específico.

### Usuarios

- **GET /users**: Obtiene una lista de todos los usuarios.
- **GET /users/{id}**: Obtiene los detalles de un usuario específico.
- **POST /users**: Crea un nuevo usuario.
- **PUT /users/{id}**: Actualiza un usuario existente.

## Estructura del Proyecto

- **Entidades**: Clases que representan los datos de la aplicación (`Reservation`, `UserT`, `ServiceT`).
- **DTOs**: Objetos de transferencia de datos (`ReservationDTO`, `UserTDTO`, `ServiceDTO`).
- **Repositorios**: Interfaces para la interacción con la base de datos (`IReservationRepository`, `IUserTRepository`, `IServiceRepository`).
- **Servicios**: Implementaciones de la lógica de negocio (`ReservationServiceImpl`, `UserTServiceImpl`, `ServiceServiceImpl`).

## Docker y Docker Compose

El proyecto incluye un archivo `Dockerfile` y un archivo `docker-compose.yml` para facilitar la construcción y despliegue de la aplicación en contenedores.

### Dockerfile

El `Dockerfile` está configurado para construir una imagen de Docker para la aplicación utilizando OpenJDK 17. Asegúrate de que el `Dockerfile` esté en la raíz del proyecto y contenga lo siguiente:

```Dockerfile
# Utiliza una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine AS builder

# Configura el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos de Maven necesarios para descargar las dependencias
COPY ./pom.xml .
COPY ./.mvn ./.mvn
COPY ./mvnw .

# Descargar las dependencias y almacenarlas en cache
RUN ./mvnw dependency:go-offline

# Copiar el código fuente al contenedor
COPY ./src ./src

# Construir el proyecto y empaquetar el JAR
RUN ./mvnw clean package -DskipTests

# Fase final de la imagen, donde se creará la imagen resultante.
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copia el archivo jar generado en la fase de construcción (builder) al directorio /app de la imagen final.
COPY --from=builder /app/target/hotel-0.0.1-SNAPSHOT.jar ./app.jar

ENV PORT 8009

# Exponer el puerto para la aplicacion
EXPOSE $PORT

# Define el comando de inicio para el contenedor, que ejecutará la aplicación Java.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

```

## Como ejecutar

- **Clona el repositorio.**:
- **Navega al directorio del proyecto.**:
- **Construye y ejecuta los contenedores con Docker Compose:**:
### docker-compose up --d
- **Esto levantará los servicios de la aplicación y la base de datos en contenedores Docker.:**:
