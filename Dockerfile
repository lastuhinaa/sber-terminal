FROM eclipse-temurin:11-alpine as jre-build

RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.desktop,java.logging,java.management,java.naming,java.security.jgss,java.instrument,java.sql,jdk.unsupported\
         --output /java

FROM alpine:3.17.3

ENV JAVA_HOME=${USER_HOME}/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /java $JAVA_HOME

ARG JAR_FILE=target/*.jar
ENV JAR_FILE ${JAR_FILE}

COPY  ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]