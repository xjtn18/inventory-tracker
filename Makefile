# Makefile for Java Maven Spring-boot projects

build:
	@ mvn compile

run:
	@ mysql -hlocalhost -uroot < src/main/resources/init-db.sql
	@ mvn spring-boot:run

clean:
	@ mvn clean
