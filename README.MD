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
      "email": "example@gmail.com",
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
    "email": "example@gmail.com",
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

<h4> POST Logout at  http://localhost:8080/api/v1/auth/user </h4>

- requires the Barer token authorization header sent by the request.
- it sets the token's revoke and expired to true in the database.
- response:
    - status 200 ok