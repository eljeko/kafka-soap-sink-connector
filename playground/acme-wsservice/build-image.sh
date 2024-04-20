echo "Stopping image"

docker container stop acme-soap-service

docker image rm acme/acme-service:1.0

docker ps -a|grep acme-service|awk '{print "docker rm " $1}'|sh

./mvnw clean package -Dquarkus.package.type=uber-jar

echo "App built, creating image"

docker image build -t acme-service:1.0 .

echo "Tagging image"

docker tag acme-service:1.0 acme/acme-service:1.0

echo "Done, check images and tags: "
docker image list |grep acme