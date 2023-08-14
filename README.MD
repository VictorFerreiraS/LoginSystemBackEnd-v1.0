<h3> Login System Microservices! </h3> 

This is a Microservice styled backend application that will be responsible for user authentication and registration
alongside email sending.

Features:

<h4> User registration POST at http://localhost:8080/api/v1/auth/register </h4>

- Request body example:
```
{ 
      "firstName": "firstName",
      "lastName": "lastName",
      "email": "email@gmail.com",
      "password": "password"
}
```
- Response:
    - JWT token
    - Registration at Mysql Database inside schema user_registration table _user;

<h4> User authentication POST at http://localhost:8080/api/v1/auth/authenticate </h4>

- request body example:
    - request body example:
```
{
    "email": "email@gmail.com",
    "password": "password"
}
```
- response:
  - JWT token;
  - user data;

<h4> GET User information at http://localhost:8080/api/v1/user/get-user </h4>

- requires the Barer token authorization header sent by the request.
- response:
    - user information in json

```
{
    "id":1,
    "firstName": "firstName",
    "lastName": "lastName",
    "email": "email@gmail.com",
    "password": "$2a$10$nVRoZO17GIKs4AB0IeuUjuELLHu62BHYOzdpsN3RtPwi0ReLXpsHW",
    "confirmed": false,
    "role": "USER",
    "enabled": true,
    "username": "email@gmail.com",
    "authorities": [{"authority":"USER"}],
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true
}
```

<h4> GET Delete user at http://localhost:8080/api/v1/user/delete-user </h4>

- requires the Barer token authorization header sent by the request.
- Deletes the user and token corresponding to the token in the auth header;

<h4> POST Logout at  http://localhost:8080/api/v1/auth/user/logout </h4>

- requires the Barer token authorization header sent by the request.
- it sets the token's revoke and expired to true in the database.
- response:
    - status 200 ok

<h4> GET Send Confirmation Email http://localhost:8080/api/v1/auth/user/send-confirmation-email </h4>

- requires the Bearer token authorization header sent by the request.
- Sends the confirmation email to the user;
<h3> Running on your machine </h3>

<h4> Requirements </h4>

- Java 17 installed
- MySql server installed

<h4>Database configuration query</h4>

- Running the following query in your mysql terminal or mysql workbench:

```
-- Create the "user_registration" database
CREATE DATABASE user_registration;

-- Create the "email" database
CREATE DATABASE email;

-- Grant all privileges for "user_registration" database to user "root"
GRANT ALL PRIVILEGES ON user_registration.* TO 'root'@'localhost';

-- Grant all privileges for "email" database to user "root"
GRANT ALL PRIVILEGES ON email.* TO 'root'@'localhost'; 
```

<h4> Running in the terminal Ubuntu </h4>

- Change the working directory in the start.sh file
- Have java 17 installed in your machine
- run: chmod u+x start.sh
  - create the .run for the email-ms microservice
``` 
export GOOGLE_PASSWORD= you 16 digt password for you google account 

mvn spring-boot:run
```
- run: chmod u+x run.sh in EVERY microservice folder listed!
- the in the terminal in the parent component LoginSystemBackEnd run: ./start.sh 