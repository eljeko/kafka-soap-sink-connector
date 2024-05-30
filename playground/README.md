# SOAP Sink Connector Playground

# Build the connector

The `docker-compose.yml` uses the connector build to install it on startup

So build the connector before you go

```bash
./01_build-connecotor.sh
```

# Setup the SOAP service acme-wsservice 

```
./02_build-acme-ws.sh
```

# Start the cluster

```bash
./03_start-cluster.sh
```

You may have some WARN logs while the admin client is tryng to create the topics for the connector.

The docker compose will use the connect hub package previously added

# Create the topics used by connector

```
./04_create-topic.sh
```

# Setup the connector

Che if the connector is available:

```bash
curl localhost:8083/connector-plugins|jq
```

Create the topic for the connector if the strat script failed (you can change the topic partition setup based on you needs) 

```bash
docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic messages --partitions 1 --replication-factor 1
```

```bash
docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic replies --partitions 1 --replication-factor 1
```


## Instantiate Soap Sink connector

Run this command:


```bash
curl -i -X PUT -H  "Content-Type:application/json" \
    http://localhost:8083/connectors/soapsink/config \
    -d '{
        "name": "soapsink",
        "soap.endpoint.url": "http://acme-soap-service:8080/soap/acme",
        "xslt.file.path": "/home/transform.xslt",
        "kafka.sink.bootstrap": "broker:9092",
        "kafka.sink.topic": "replies",
        "connector.class": "org.connect.soap.SoapSinkConnector",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "topics": "messages",
        "file": "/tmp/test",
        "value.converter.schemas.enable": "false"
    }'
```

Wait on `replies` topic:

```
docker exec -it broker kafka-console-consumer --bootstrap-server broker:9092  --topic replies  --property "print.key=true" --from-beginning
```

Send some request on `messages` topic:

```
jr run --embedded '{{counter "mycounter" 0 1}}*{"message":"Hello here User-{{integer 1 999}}"}' -n 10| kafka-console-producer --bootstrap-server broker:9092 --topic messages --property "key.separator=*" --property "parse.key=true"
```

