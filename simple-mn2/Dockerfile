FROM openjdk:14-alpine
COPY build/libs/simple-mn2-*-all.jar simple-mn2.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "simple-mn2.jar"]
