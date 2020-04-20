package com.samples.reactor;

import com.examples.common.Utils;
import com.examples.common.SampleException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

import static com.examples.common.Utils.toInteger;

@Slf4j
public class SampleApp {
    private static final int DEFAULT_COUNT = 137;
    private static final Scheduler scheduler = Schedulers.newElastic(SampleApp.class.getSimpleName());
    private static final int DEFAULT_CONCURRENCY = 8;

    private int total = 0;
    private final Map<String, Integer> usage = new HashMap<>();

    public void run(final String... args) {
        for (String arg : args) {
            log.info("{}", arg);
        }
    }

    private synchronized void updateUsage(final int value) {
        final String name = Thread.currentThread().getName();
        log.info("{} : {} : {} : {}", name, value, total, total - value);
        total++;
        usage.merge(name, 1, Integer::sum);
    }

    private void logUsage(final int expectedTotal) {
        final Map<String, Integer> map = new HashMap<>();
        usage.forEach((k, v) -> map.merge("total", v, Integer::sum));

        final int total = map.get("total");
        if (total != expectedTotal) {
            log.error("Invalid total expected:{} got:{}",expectedTotal, total);
            throw new SampleException("Invalid total");
        }

        usage.forEach((k, v) -> {
            final String pct = String.format("%1$6s", Utils.percentage(v, total, 2));
            final String name = String.format("%1$-12s", k);
            log.info("Usage {} : {} {}", name, pct, String.format("%1$8d", v));
        });
        log.info("Total {}", String.format("%1$30d", total));
    }

    public void fluxOne(final int count, final int concurrency) {
        Flux.range(0, count)
                .flatMap(n -> Mono.just(n).subscribeOn(scheduler), concurrency)
                .doOnComplete(() -> logUsage(count))
                .subscribe(
                        this::updateUsage,
                        t -> log.error("ERROR:{}", t.getMessage()),
                        () -> log.info("{}", "Done"));
    }

    public static void main(final String... args) {
        final SampleApp app = new SampleApp();
        Integer c = args.length > 0 ? toInteger(args[0]) : DEFAULT_COUNT;
        Integer y = args.length > 1 ? toInteger(args[1]) : DEFAULT_CONCURRENCY;
        app.fluxOne(c == null || c <=0 ? DEFAULT_COUNT : c, y == null || y <= 0 ? DEFAULT_CONCURRENCY : y);
    }
}
