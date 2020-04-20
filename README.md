# Sample Apps

## Bitnami Docker
### Compose
```
docker-compose up -d
...
Creating sample-apps_zookeeper_1 ... done
Creating sample-apps_kafka_1     ... done
Creating sample-apps_things_1    ... done
Creating sample-apps_consumers_1 ... done
```

```
docker-compose stop
```

### Scale consumers
```
docker-compose up -d --scale consumers=3
```

### Create topic
```
docker exec -it sample-apps_kafka_1 bash

cd /opt/bitnami/kafka/bin/
./kafka-topics.sh --create --zookeeper zookeeper:2181 --topic sample-events --partitions 3 --replication-factor 1
./kafka-topics.sh --describe --zookeeper zookeeper:2181 --topic sample-events
```

### Watch Logs
```
docker logs --follow sample-apps_things_1
```

```
docker logs --follow sample-apps_consumers_1
```
