FROM openjdk:21-jdk AS backend

#diretorio onde esta a aplicação
WORKDIR /technical-challenge-back

#copia o jar
COPY build/libs/technical-challange-0.0.1-SNAPSHOT.jar .

COPY .env .

#expoe a porta 8080 da aplicação
EXPOSE 8080

#inicia a aplicação
ENTRYPOINT ["java", "-jar", "technical-challange-0.0.1-SNAPSHOT.jar"]