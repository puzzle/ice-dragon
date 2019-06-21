#!/bin/bash

./mvnw clean package -Pprod

cp src/main/docker/* target/

docker build -t guggero/icedragon:latest target
docker push guggero/icedragon:latest
