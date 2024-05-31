docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic requests --partitions 1 --replication-factor 1

docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic responses --partitions 1 --replication-factor 1