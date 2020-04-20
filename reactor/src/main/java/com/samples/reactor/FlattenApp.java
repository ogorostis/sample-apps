package com.samples.reactor;

import com.examples.common.Sums;
import com.examples.common.Utils;
import com.examples.common.SampleException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.examples.common.Utils.toInteger;

@Slf4j
public class FlattenApp {
    private static final int DEFAULT_COUNT = 10;
    private static final int DEFAULT_CONCURRENCY = 8;
    private static final Scheduler scheduler = Schedulers.newElastic("squared");

    private int total = 0;
    private final Map<String, Integer> usage = new HashMap<>();

    private synchronized void updateUsage(final String value) {
        total++;
        final String name = Thread.currentThread().getName();
        log.info("{} : {} : {}", name, value, total);
        usage.merge(name, 1, Integer::sum);
    }

    private void uponCompletion(final int expectedTotal) {
        final Map<String, Integer> map = new HashMap<>();
        usage.forEach((k, v) -> map.merge("total", v, Integer::sum));

        final int total = map.get("total");
        if (total != expectedTotal) {
            log.error("Invalid total expected:{} got:{}",expectedTotal, total);
            throw new SampleException("Invalid total");
        }

        usage.forEach((k, v) -> {
            final String pct = String.format("%1$7s", Utils.percentage(v, total, 2));
            final String name = String.format("%1$-12s", k);
            log.info("Usage {} : {} {}", name, pct, String.format("%1$8d", v));
        });
        log.info("Total {}", String.format("%1$31d", total));
    }

    /**
     * Create a String flux whose size is n square
     * @param m number
     * @return String flux
     */
    private Flux<String> squaredStringFlux(final int n) {
        final int size = n * n;
        final List<String> list = new LinkedList<>();
        for (int i=0; i<size; i++) {
            list.add("s" + n + "-" + (i+1));
        }
        return Flux.fromIterable(list);
    }

//    private void trace(final String s) {
//        log.info("{} : {}", Thread.currentThread().getName(), s);
//    }

    void flux(final int count, final int concurrency) {
        // For each number in initial flux
        // - create a new String flux whose size is its square
        Flux.range(1, count)
                .flatMap(n -> squaredStringFlux(n).subscribeOn(scheduler), concurrency)
                .subscribe(
                        this::updateUsage,
                        t -> log.error("ERROR:{}", t.getMessage()),
                        () -> uponCompletion(Sums.firstNSquares(count)));
    }

    public static void main(final String... args) {
        final FlattenApp app = new FlattenApp();
        Integer c = args.length > 0 ? toInteger(args[0]) : DEFAULT_COUNT;
        Integer y = args.length > 1 ? toInteger(args[1]) : DEFAULT_CONCURRENCY;
        app.flux(c == null || c <=0 ? DEFAULT_COUNT : c, y == null || y <= 0 ? DEFAULT_CONCURRENCY : y);
    }
}
