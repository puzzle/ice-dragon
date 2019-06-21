FROM php:7.3-apache

COPY pages/ /var/www/html/

RUN sed -ri -e 's/Listen 80/Listen 8080/g' /etc/apache2/ports.conf

EXPOSE 8080

