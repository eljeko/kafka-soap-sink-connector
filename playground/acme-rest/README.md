# Build

mvn clean package

# Run

You need a running Kafka Cluster

Setup the environment variable with BOOTSTRAP_SERVER:

    export BOOTSTRAP_SERVER=broker:9092

# Test

Call the service:

   curl -X GET http://localhost:8084/acmerest/123

You should receive:

    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns2:helloResponse xmlns:ns2="http://services.acme.com/"><return>Hello Hello here User-123!</return></ns2:helloResponse></soap:Body></soap:Envelope>eljeko@hydra$