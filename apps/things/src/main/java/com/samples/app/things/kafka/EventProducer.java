package com.samples.app.things.kafka;

import com.examples.common.SampleException;
import com.examples.request.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class EventProducer {
    @Value("${topics.guest-events}")
    private String guestEventsTopic;

    private final KafkaTemplate<String, String> template;

    @Autowired
    public EventProducer(final KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public Object produce(final Event event) {
        if (event.getDate() == null) {
            event.setDate(new Date());
        }

        try {
            template.send(guestEventsTopic, event.getKey(), event.serialize());
            return new HashMap<String, Object>() {{
                put("key", event.getKey());
                put("topic", guestEventsTopic);
            }};
        } catch (JsonProcessingException e) {
            throw new SampleException(500, "Serialization error", e);
        }
    }
}
