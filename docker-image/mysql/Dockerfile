#: the basic image
FROM mysql:5.7

#: make directory
RUN mkdir -p /var/log/mysql/

#: replace the my.cnf with configuration
COPY ./my.cnf /etc/mysql/
