package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class WordCount {

    public static Map<String, Long> countWordsFrequencies(Path path) throws IOException {
        BufferedReader br = Files.newBufferedReader(path.toAbsolutePath());
        Map<String, Long> mapList = br.lines()
                .filter(line -> !line.isEmpty())
                .map(line -> line.toLowerCase())
                .map(line -> line.split("[  .,\\r\\n]+|[a-zA-Z]+]"))
                .flatMap(Arrays::stream)
                .collect(groupingBy(Function.identity(), Collectors.counting()));
        return mapList;
    }

    public static List<String> sortWordFrequencies(Map<String, Long> wordMap,
                                                   Comparator<Map.Entry<String, Long>> firstComparator,
                                                   Comparator<Map.Entry<String, Long>> secondComparator, long limit) {
        List<String> list = wordMap.entrySet().stream()
                .sorted(firstComparator.thenComparing(secondComparator))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .limit(limit)
                .collect(toList());
        return list;
    }

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            throw new IllegalArgumentException("Use program: wordCount <filePath> <optional_word_limit>");
        }

        String filepath = args[0]; // "Lyrics.txt";
        int n = args.length == 1 ? -1 : Integer.parseInt(args[1]);//5;

        Path path = Paths.get(filepath);
        Map<String, Long> mapList = countWordsFrequencies(path);

        final Comparator<Map.Entry<String, Long>> countComparator = comparing(Map.Entry::getValue, reverseOrder());
        final Comparator<Map.Entry<String, Long>> alphabetComparator = comparing(Map.Entry::getKey);

        int limit = n != -1 ? n : mapList.size();

        List<String> list = sortWordFrequencies(mapList, countComparator, alphabetComparator, limit);
        list.forEach(System.out::println);
    }
}