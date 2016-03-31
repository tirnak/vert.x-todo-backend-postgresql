# vert.x-todo-backend-postgresql
Sample web application compatible with [todo-backend](http://www.todobackend.com/) specs

Implemented, using Vert.x 3 and PostgreSQL. [Deployed on Heroku] (https://vertx-todo-backend-postgresql.herokuapp.com/).

Tests for [todo-backend](http://www.todobackend.com/) are shown [here](http://www.todobackend.com/specs/index.html?https://vertx-todo-backend-postgresql.herokuapp.com/)


To run locally:
```bash
$ sudo apt-get install postgresql (yum, pacman, etc...)
//some postgresql configuration
//set local.properties or test.properties
$ mvn clean package
$ cd target
$ java -jar vert.x-todo-1.0-SNAPSHOT-fat.jar
```
