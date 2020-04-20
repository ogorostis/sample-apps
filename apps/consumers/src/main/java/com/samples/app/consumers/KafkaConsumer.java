package com.samples.app.consumers;

import com.examples.request.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {
    @KafkaListener(topics = "sample-events")
    public void listen(final ConsumerRecord<String, String> record) throws Exception {
        final Event event = Event.deserialize(record.value());
        log.info("Type:{}", event.getType());
        log.info("Name:{}", event.getName());
        log.info("Email:{}", event.getEmail());
        log.info("Description:{}", event.getDescription());
        log.info("Date:{}", event.getDate());
    }
}
