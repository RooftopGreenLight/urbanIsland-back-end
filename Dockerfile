FROM openjdk:11

WORKDIR /usr/src/server

COPY ./build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--jasypt.encryptor.password=${password}"]