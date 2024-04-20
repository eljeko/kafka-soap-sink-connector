docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic messages --partitions 1 --replication-factor 1

docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic replies --partitions 1 --replication-factor 1