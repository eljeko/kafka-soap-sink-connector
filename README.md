# SOAP Sink Connector

This is an example connector to sink json message transformed into soap xml to a SOAP endpoint

## How it works 

The connector convert the json message into xml then will uses a xstl transformation create a soap envelope to send to the SOAP service endpoint.

The response from the SOAP service is then published on a reply topic

A lot of feature (and tests) are missing, but it's a starting point to test the round trip.

# Build the connector

```bash
mvn clean package -DskipTests
```

This will generate a confluent hub package under:

```
target/components/packages/org-kafka-connect-soap-sink-0.1.zip
```

If you want to try the connector there is a `playground` dir with a `docker-compose` and a dummy SOAP Service compile and use in the example.