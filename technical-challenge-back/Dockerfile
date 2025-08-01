FROM gradle:8.4-jdk21 AS builder

WORKDIR /technical-challenge-back

COPY . .

# builda a aplicação para que tenha a versão mais atualizada possível
RUN gradle build --no-daemon

FROM openjdk:21-jdk AS backend

#diretorio onde esta a aplicação
WORKDIR /technical-challenge-back

#copia o jar do builder
COPY --from=builder /technical-challenge-back/build/libs/technical-challange-0.0.1-SNAPSHOT.jar .

COPY .env .

#expoe a porta 8080 da aplicação
EXPOSE 8080

#inicia a aplicação
ENTRYPOINT ["java", "-jar", "technical-challange-0.0.1-SNAPSHOT.jar"]