# Makefile for Java Maven Spring-boot projects

build:
	@ mvn compile

run:
ifeq ($(strip $(db)), reset)
	@ mysql -hlocalhost -uroot < src/main/resources/init-db.sql
endif
	@ mvn spring-boot:run

clean:
	@ mvn clean
