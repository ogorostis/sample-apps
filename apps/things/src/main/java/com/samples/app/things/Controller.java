package com.samples.app.things;

import com.examples.common.SampleException;
import com.examples.request.Event;
import com.samples.app.things.kafka.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class Controller {
    private final EventProducer producer;

    @Value("${app.message}")
    private String message;

    @Autowired
    public Controller(final EventProducer producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/home")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> home() {
        return new HashMap<String, Object>() {{
            put("message", message);
        }};
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> echo(@PathVariable final String id) {
        return new HashMap<String, Object>() {{
            put("id", id);
            put("ts", System.currentTimeMillis());
        }};
    }

    @PostMapping(value = "/events")
    @ResponseStatus(HttpStatus.OK)
    public Object postEvent(@RequestBody final Event event) {
        return producer.produce(event);
    }

    @ControllerAdvice
    static class ThingsExceptionHandler extends ResponseEntityExceptionHandler {
        @ExceptionHandler(value = RuntimeException.class)
        ResponseEntity<Object> advise(final RuntimeException rte, final WebRequest request) {
            return handleExceptionInternal(rte,
                    new HashMap<String, String>() {{put("error", rte.getMessage());}},
                    new HttpHeaders(), statusOf(rte), request);
        }

        private static HttpStatus statusOf(final RuntimeException rte) {
            if (rte instanceof SampleException) {
                return HttpStatus.resolve(((SampleException)rte).getStatus());
            }
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
