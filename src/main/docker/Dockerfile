FROM openjdk:11-jre-slim

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED" \
    TZ=Europe/Zurich

# Add a icedragon user to run our application so that it doesn't need to run as root
RUN adduser --disabled-password --gecos "" --gid 0 --shell /bin/sh icedragon
WORKDIR /home/icedragon

RUN mkdir /app-data
ADD entrypoint.sh entrypoint.sh
RUN chown icedragon:root entrypoint.sh /app-data && chmod 775 entrypoint.sh /app-data
USER icedragon

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

ADD icedragon-*.jar /app.jar
