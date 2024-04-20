docker-compose down

docker volume ls|awk '{print "docker volume rm " $2}'|sh