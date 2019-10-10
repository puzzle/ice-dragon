#!/bin/bash

./mvnw clean package -Pprod -DskipTests

cp src/main/docker/* target/

docker build -t selbert/icedragon:latest target
docker push selbert/icedragon:latest

cd content-provider-demo-pages

docker build -t selbert/icedragon-content-demo:latest .
docker push selbert/icedragon-content-demo:latest

cd ..

oc import-image ice-dragon
oc import-image ice-dragon-content-demo
