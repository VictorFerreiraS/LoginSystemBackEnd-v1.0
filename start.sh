#!/bin/bash

# Define the list of services
SERVICES=(eureka-server-ms email-ms regs-auth-ms spring-gateway-ms)

# Loop through each service and run it using mvn spring-boot:run
for SERVICE in "${SERVICES[@]}"
do
  echo "Running $SERVICE..."
  gnome-terminal --tab --working-directory="/home/victor/projects/fullstack/login-system/backend/LoginSystemBackEnd/${SERVICE}" -- bash -c "cd /home/victor/projects/fullstack/login-system/backend/LoginSystemBackEnd/${SERVICE}; ./run.sh exec bash"
done
