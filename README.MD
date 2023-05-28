<h3> Login System Microservices! </h3> 

This is a Microservice styled backend application that will be responsible for user authentication and registration
alongside email sending.

Features:

- User registration POST at http://localhost:8080/api/v1/auth/register
    - request body example:
        - {
          "firstName": "firstName",
          "lastName": "lastName",
          "email": "example@gmail.com",
          "password": "password"
          }
    - response:
        - JWT token
        - Registration at Mysql Database inside schema user_registration table _user;

- User authentication POST at http://localhost:8080/api/v1/auth/authenticate
    - request body example:
        - request body example:
            - {
              "email": "example@gmail.com",
              "password": "password"
              }
        - response:
            - JWT token;
            - user data;
- GET User information at http://localhost:8080/api/v1/user/get-user
    - requires the Barer token authorization header sent by the request.
    - response:
        - user information in json
            - {"id":1,"firstName":"Victor","lastName":"Ferreira","email":"victor.fagundes586@gmail.com","password":"
              $2a$10$nVRoZO17GIKs4AB0IeuUjuELLHu62BHYOzdpsN3RtPwi0ReLXpsHW","confirmed":false,"role":"USER","enabled":
              true,"username":"victor.fagundes586@gmail.com","authorities":[{"authority":"USER"}],"accountNonExpired":
              true,"accountNonLocked":true,"credentialsNonExpired":true}
- GET Delete user at http://localhost:8080/api/v1/user/delete-user
    - requires the Barer token authorization header sent by the request.
    - Deletes the user and token corresponding to the token in the auth header;

- POST Logout at  http://localhost:8080/api/v1/auth/user
    - requires the Barer token authorization header sent by the request.
    - it sets the token's revoke and expired to true in the database.
    - response:
        - status 200 ok