# Imagen base con Java 17
FROM eclipse-temurin:17-jdk-alpine

# Carpeta de trabajo
WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Dar permisos a mvnw
RUN chmod +x mvnw

# Compilar sin tests
RUN ./mvnw clean package -DskipTests

# Exponer puerto dinámico
EXPOSE 8080

# Ejecutar el jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
