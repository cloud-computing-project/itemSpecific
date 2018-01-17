# RSO: itemSpecific microservice

## Prerequisites

```bash
docker run -d --name itemSpecific -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=itemSpecific -p 5432:5432 postgres:latest
```

## Run application in Docker

```bash
docker run -p 8080:8080 -e KUMULUZEE_CONFIG_ETCD_HOSTS=http://192.168.99.100:2379 amela/item_specific
```