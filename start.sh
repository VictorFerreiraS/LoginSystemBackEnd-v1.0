#!/bin/bash

# Get the directory of the currently executing script
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

# Define the list of services
SERVICES=(eureka-server-ms email-ms regs-auth-ms spring-gateway-ms)

# Loop through each service and run it using mvn spring-boot:run
for SERVICE in "${SERVICES[@]}"
do
  echo "Running $SERVICE..."
  gnome-terminal --tab --working-directory="$SCRIPT_DIR/${SERVICE}" -- bash -c "cd $SCRIPT_DIR/${SERVICE}; ./run.sh; exec bash"
done
