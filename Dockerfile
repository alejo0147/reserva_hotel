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
