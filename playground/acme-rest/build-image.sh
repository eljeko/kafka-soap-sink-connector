echo "Stopping image"

docker container stop acme-rest-service

docker image rm acme/acme-rest-service:1.0

docker ps -a|grep acme-rest-service|awk '{print "docker rm " $1}'|sh

./mvn clean package

echo "App built, creating image"

docker image build -t acme-rest-service:1.0 .

echo "Tagging image"

docker tag acme-rest-service:1.0 acme/acme-rest-service:1.0

echo "Done, check images and tags: "
docker image list |grep acme-rest-service