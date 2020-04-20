package com.examples.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
public class Event {
    private static final ObjectMapper mapper = new ObjectMapper();

    @NonNull
    private String type;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String description;

    private Date date;

    @JsonIgnore
    public String getKey() {
        return type + ":" + email;
    }

    public String serialize() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }

    public static Event deserialize(final String s) throws JsonProcessingException {
        return mapper.readValue(s, Event.class);
    }
}
