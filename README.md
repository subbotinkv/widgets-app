# Widget App

**Summary** : Widget management REST API

**Requirements**:

* Language - Java 11; Framework - Spring Boot; Build tool - Maven; 
* Do only the server-side, you don’t need to do visualization;
* No need to do any authorization;
* No explicit limits on memory or runtime, but the more efficient the implementation, the better;
* You can assume that 90% of load distribution is read operations;
* The API should respect to REST architecture;
* Data should be stored in memory. You can use any classes of the standard library to organize storage. Using any external repositories and databases is forbidden.
* All changes to widgets must occur atomically. The Z-index must be unique.
* At least 30% of the code should be covered by unit and integration tests;
* Submit sources via a public git repository.

## Description

### TL;DR

Build:

    $mvn clean install
Run:

    $mvn spring-boot:run
API:

    http://localhost:8080/api/widgets
Swagger:

    http://localhost:8080/swagger-ui/

### General

Tha app is a Spring Boot Application. It runs in an embedded webserver (tomcat).   

### REST API

The API in the app is designed according to REST-principles. [Zalando RESTful API Guidelines](https://opensource.zalando.com/restful-api-guidelines/#) were used during the design of API. The API also has Swagger-documentation - [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/).
[Problem JSON](https://github.com/zalando/problem-spring-web/) is used to represent any problem occurred during processing of REST API calls in a unified way.

### Business logic

All the logic related to widget management (z-index changes) is located in service layer. ReadWriteLock is used to manage concurrent access to widgets data.

### Storage

#### In-Memory storage

HashMap collection class is used to organize the storage.

### Test Coverage

![img.png](Test Coverage.png)