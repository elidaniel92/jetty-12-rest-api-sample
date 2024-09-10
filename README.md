# jetty-12-rest-api-sample

## TLDR

Windows - Powershell
```
docker-compose build | docker-compose up
```
Linux - Bash
```
docker-compose build && docker-compose up
```

This project serves as a boilerplate for building pure Jetty 12 (Servlet) using the following technologies and tools:

- [**Java 17**](https://www.oracle.com/br/java/technologies/downloads/): The latest long-term support version of Java, ensuring modern language features and improvements.
- [**Jetty 12**](https://jetty.org/): Eclipse Jetty provides a highly scalable and memory-efficient web server and servlet container, supporting many protocols such as HTTP/3,2,1 and WebSocket.
- [**Jackson**](https://github.com/FasterXML/jackson): The Java JSON library.
- [**Maven**](https://maven.apache.org/): Used for project management and dependency management.
- [**Docker Compose**](https://docs.docker.com/compose/): Simplifies local development by allowing you to run the entire application stack (including MySQL) within Docker containers.
- [**Dirk**](https://github.com/hjohn/Dirk): A lightweight dependency injection library for managing object creation and dependencies.
- [**SLF4J with Log4j2**](https://logging.apache.org/log4j/2.x/): Provides robust and flexible logging through the SLF4J API, using Log4j2 as the logging backend.
- [**JDBC**](https://docs.oracle.com/javase/tutorial/jdbc/): Utilized for establishing a connection to relational databases and performing SQL operations.
- [**MySQL Database**](https://www.mysql.com/): The database for storing and managing application data.


This setup is ideal for jump-starting a Java project with dependency injection, logging, and database connectivity, all contained in a local development environment via Docker Compose.

# Getting started

## Installation

[Install Docker](https://www.docker.com/)

[Install JDK 17](https://www.oracle.com/br/java/technologies/downloads/) (optional)

[Install Maven](https://maven.apache.org/) (optional)
> **Tip:** you can run Maven commands without having Maven installed.
```
./mvnw foo
```


Clone the repository

```
git clone https://github.com/elidaniel92/jetty-12-rest-api-sample.git
```

Switch to the repo folder

```
cd jetty-12-rest-api-sample
```
    
### Install dependencies (optional)

```
mvn clean install
```

## Running the App with Docker Compose

Build the App and MySQL Container Image

```
docker-compose build
```

Started the container
```
docker-compose up
```

## Running the Java Application locally and  MySQL Server in the container

### Build the MySQL Container Image

Since you cannot load an environment file using the `build command` command, go to the MySQL Dockerfile and uncomment the environment variables section.

```
docker build -t db-img -f docker/Dockerfile.mysql .
```
### Start the MySQL Server Container
```
docker run --name db-container -ip 3306:3306 db-img
```
After the message below, the server is ready for connections. You can close the terminal:
```
2024-09-07T22:48:15.362678Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.4.2'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
```
### Setting the Java Application Environment Variable

Load the [local environment variables file (env/local.env)](env/local.env) in your terminal session.

Windows - To load the env/local.env file in a PowerShell terminal session, use the command below:
```
Get-Content env/local.env | ForEach-Object { if ($_ -match '^(.*?)=(.*)$') { [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2]) } }
```
Linux - To load the env/local.env file in a Bash terminal session, use the command below:
```
export $(grep -v '^#' env/local.env | xargs)
```

### Running the app

#### With .jar

Generete .jar
```
mvn clean package
```
Run .jar
```
java -jar target/jetty-12-rest-api-sample-1.0-SNAPSHOT.jar
```
#### With Maven
```
mvn clean compile exec:java
```

### Delete All Images and Containers

```
docker rm -f db-container  
docker rmi db-img
docker rm -f store-system-app-1
docker rm -f store-system-db-1
docker rmi -f store-system-app
docker rmi -f store-system-db
```

# License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

