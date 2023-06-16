FROM openjdk:19 as builder
WORKDIR application
ADD ./target/changshan-web.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:19

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS='-server -Xms1024m -Xmx1024m'
ENV LANG C.UTF-8
EXPOSE 8080
WORKDIR application
COPY --from=builder application/dependencies/ ./
RUN true
COPY --from=builder application/snapshot-dependencies/ ./
RUN true
COPY --from=builder application/spring-boot-loader/ ./
RUN true
COPY --from=builder application/application/ ./
RUN true
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher"]

