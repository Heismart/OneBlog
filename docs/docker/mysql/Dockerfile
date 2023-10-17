# FROM mysql:8.0.20
FROM ubuntu/mysql:latest

MAINTAINER luogenji@hotmail.com

ENV TZ=Asia/Shanghai

ADD ./cnf/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./dblog.sql /docker-entrypoint-initdb.d

COPY ./init.sql /docker-entrypoint-initdb.d
