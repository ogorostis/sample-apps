package com.samples.reactor;

import com.examples.common.Utils;
import com.examples.common.SampleException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ArticleApp {

    @Data
    private class LetterCount {
        private final Character letter;
        private final int value;
    }

    private static Function<String, Map<Character,Integer>> mapByLetter = word -> {
        final HashMap<Character, Integer> letterCounts = new HashMap<>();
        final String letters = word.toLowerCase();
        for (int i=0; i<letters.length(); i++) {
            final Character c = letters.charAt(i);
            letterCounts.merge(c, 1, Integer::sum);
        }
        return letterCounts;
    };

    private BinaryOperator<Map<Character,Integer>> mapAdd = (accumulator, added) -> {
        for (final Character c : added.keySet()) {
            if (accumulator.get(c) == null) {
                accumulator.put(c, added.get(c));
            } else {
                accumulator.put(c, accumulator.get(c) + added.get(c));
            }
        }
        return accumulator;
    };

    private Collection<String> tokenizeArticle(final String article) {
        final String delimiters = " \t\n\r\f\"-:',;.~»«’";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(article)))) {
            final Collection<String> words = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                final StringTokenizer tokenizer = new StringTokenizer(line, delimiters);
                while (tokenizer.hasMoreTokens()) {
                    words.add(tokenizer.nextToken());
                }
            }
            return words;
        } catch (Exception e) {
            log.error("Could not read article {} : {}", article, e.getMessage());
            throw new SampleException("Could not read article " + article, e);
        }
    }

    private String article(final String country) {
        if ("fr".equals(country)) {
            return "/articles/fr.txt";
        }
        if ("it".equals(country)) {
            return "/articles/it.txt";
        }
        if ("es".equals(country)) {
            return "/articles/es.txt";
        }
        throw new SampleException("Unsupported country " + country);
    }

    private void trace(final Map<Character,Integer> map) {
        // map.forEach((k,v) -> log.info("{} : {}", k, v));
        log.info("SIZE:{}", map.size());
    }

    private void trace(final List<LetterCount> list) {
        int total = 0;
        for (final LetterCount count : list) {
            total += count.value;
        }
        final int den = total;
        list.forEach(c -> log.info("{} {} {}",
                c.letter,
                String.format("%1$6s", Utils.percentage(c.value, den, 1)),
                String.format("%1$5d", c.value)));
    }

    public void analyzeArticle(final String country) {
        final Collection<String> words = tokenizeArticle(article(country));
        log.info("{} has {} words", article(country), words.size());
        final Map<Character,Integer> initial = new HashMap<>();
        Flux.fromIterable(words)
                .doOnComplete((() -> trace(initial)))
                .map(mapByLetter)
                .reduce(initial, mapAdd)
                .map(map -> map.keySet().stream()
                        .map(letter -> new LetterCount(letter, map.get(letter)))
                        .sorted(Comparator.comparingInt(c -> - c.value))
                        .collect(Collectors.toCollection(LinkedList::new)))
                .subscribe(this::trace);
    }

    public static void main(final String... args) {
        final ArticleApp app = new ArticleApp();
        app.analyzeArticle(args.length > 0 ? args[0] : "fr");
    }
}
