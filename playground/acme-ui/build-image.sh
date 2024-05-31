echo "Stopping image"

docker container stop acme-ui

docker image rm acme/acme-ui:1.0

docker ps -a|grep acme-ui|awk '{print "docker rm " $1}'|sh

#npm install

echo "App built, creating image"

docker image build -t acme-ui:1.0 .

echo "Tagging image"

docker tag acme-ui:1.0 acme/acme-ui:1.0

echo "Done, check images and tags: "
docker image list |grep acme-ui