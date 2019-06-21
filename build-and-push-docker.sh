#!/bin/bash

./mvnw clean package -Pprod -DskipTests

cp src/main/docker/* target/

docker build -t guggero/icedragon:latest target
docker push guggero/icedragon:latest

cd content-provider-demo-pages

docker build -t guggero/icedragon-content-demo:latest .
docker push guggero/icedragon-content-demo:latest

cd ..

oc import-image ice-dragon
oc import-image ice-dragon-content-demo
