# 环境版本
#FROM anapsix/alpine-java:8_server-jre_unlimited
#FROM anapsix/alpine-java:latest
FROM luogenji/server-jre-8u202:alpine
MAINTAINER luogenji@hotmail.com

# JVM调优参数等额外参数
ENV PARAMS ""

ENV TZ=PRC

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

# 添加本地 JAR 到容器内
# ADD --chown=spring:spring target/blog-admin.jar app.jar
ADD  target/blog-admin.jar app.jar

# 指定容器端口
EXPOSE 8081

ENV JAVA_OPTS='-Xms128m -Xmx512m'
# 容器启动后执行的命令
ENTRYPOINT ["java","${JAVA_OPTS}", "-jar","app.jar" , "-Djava.security.egd=file:/dev/./urandom","$PARAMS"]
