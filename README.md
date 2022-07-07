# Spring Security Example with JWT Authentication

Build secure spring boot REST API with spring security and JWT (JSON Web Token) authentication

## Roles 
- spring security in this project uses roles as authorization
- Every new user who registers to the app without assigning a specific role by default will have a ROLE_USER

## API by Role authority
#### API Without Authentication  
- signup : POST `api/auth/signup`
- login : POST `api/auth/login`

#### ROLE_USER
- read user : GET `http://localhost:8080/api/users/{username}`
- update user : PUT `http://localhost:8080/api/users/{username}`
- delete user : DELETE `http://localhost:8080/api/users/{username}`

#### ROLE_ADMIN 
- all ROLE_USER scope : GET, PUT, DELETE `http://localhost:8080/api/users/**`
- read all users : GET `http://localhost:8080/api/users`
- read all roles : GET `http://localhost:8080/api/roles`
- read role : GET `http://localhost:8080/api/roles/{roleName}`
- delete role : DELETE `http://localhost:8080/api/roles/{roleName}`

## Installation
1. download or clone project from this repository
2. create MySQL database `create spring_auth_demo`
3. open project with IDE or Text Editor
4. change username and password
    - open `src/main/resources/application.properties`
    - change `spring.datasource.username` and `spring.datasource.password`
5. Build and run using maven `mvn spring:boot run`
6. test with postman
