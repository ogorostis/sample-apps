package com.samples.app.consumers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class Controller {
    @Value("${app.message}")
    private String message;

    @GetMapping(value = "/home")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> home() {
        return new HashMap<String, Object>() {{
            put("message", message);
        }};
    }
}
