# Virtual Server API

REST API to perform CRUD operations on devices and zones using Spring BOOT auto configuration and H2 in-memory database

## Specs: 

**JAVA_VERSION: ```15 --enable-preview```**

**SPRING_BOOT: ```2.3.X```**

**H2: ```1.4.200```**

## Details
Pre-configured ```Spring-Boot``` using spring auto configuration for all web and database part. Used ```EntityManager``` for 
performing DB operations as using ```Session``` and ```SessionFactory``` would require 
manual configuration of ```Data Source``` (enhancement for future)
Validation on instance fields is performed by ```hibernate validator``` and used by ```jackson``` when de-serialization is executed
on controller level. 
```ModelMapper``` is used to map DTO to Entity and vice versa as it offers easy conversion.

**Context-path: ```virt-server```**

**Port```8888```** 

**Main-Class ```virt.server.AppRunner```**

## Code
Code consists of maven modules:

**Main module: ```virt-server```**

**Data API module: ```virt-server-data-api```** (DAOs)

**Entities module: ```virt-server-entities```** (DB entities)

**Web module: ```virt-server-web```** (web components, services, dtos)


## Build and Deployment
**Intellij Idea**

```Enable preview feature for Java 15 to successfully run and compile code or let maven to run it```

**Maven Spring Boot plugin**

```java --enable-preview -jar virtserver-web/target/virtserver-web-0.0.1-SNAPSHOT-spring-boot.jar```

**Heroku**

Application is deployed by PaaS Heroku and is available on:

```https://virt-server.herokuapp.com/virt-server```

## Monitoring

Health check is created by Spring Boot actuator and is available on context-path:

```/virt-server/actuator/health```

## Docs

Spring fox swagger available on context-path:

```/virt-server/swagger-ui/index.html#/```

Spring fox api docs available on context-path

```/virt-server/v2/api-docs```

## H2 Console

To check all data in DB console is available on:

```/virt-server/h2-console```

 
