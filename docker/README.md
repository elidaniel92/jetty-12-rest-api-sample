# Docker

## Build the MySQL Container Image

You cannot load an environment file using the Docker `build command`. Therefore, go to the [MySQL Dockerfile](Dockerfile.mysql) and uncomment the environment variables section.

```
docker build -t db-img -f docker/Dockerfile.mysql .
```
## Started the container
```
docker run --name db-container -ip 3306:3306 db-img
```
After the message bellow the Server is ready for connections. You can close the terminal.
```
2024-09-07T22:48:15.362678Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.4.2'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
```

## Connect to MySQL Server
Login in MySQL Server (password is root)
```
docker exec -it b mysql -u root -p   
Enter password: root
```
Use database store
```
mysql> use store;
```
Execute query
```
mysql> select * from products;
```

## Image and Container Management

### Stop MySQL Server
```
docker stop db-container
```
### Start MySQL Server
```
docker start db-container
```

### Delete Container
```
docker rm db-container
```
### Force Stop and Remove Container
```
docker rm -f db-container
```
### Delete Image
```
docker rmi db-img
```
### Delete Image and Container
```
docker rm -f db-container && docker rmi db-img
```