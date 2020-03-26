package zad3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProgLang {

    protected Map<String, List<String>> programmers;
    protected Stream<String[]> stream1;
    protected List<String[]> list1;

    public ProgLang(String firstName) {
        programmers = new LinkedHashMap<>();
        try {
            stream1 = Files.lines(Paths.get(firstName)).collect(Collectors.toList())
                    .stream()
                    .map(line -> line.split("\t"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        list1 = stream1.collect(Collectors.toList());
        list1.stream().forEach(text ->
                programmers.put(text[0], Arrays.asList(text).subList(1, text.length).stream().distinct().collect(Collectors.toList())));
    }


    Map<String, List<String>> getLangsMap() {
        return programmers;
    }

    Map<String, List<String>> getProgsMap() {
        List<String> programmersList = programmers.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());

        Map<String, List<String>> result = new LinkedHashMap<>(programmersList.size());
        for (String str : programmersList)
            result.put(str, new ArrayList<>());

        for (String str : programmersList)
            for (String str1 : programmers.keySet())
                if (programmers.get(str1).contains(str))
                    result.get(str).add(str1);
        return result;
    }


    Comparator<Map.Entry<String, List<String>>> valueComparison = Comparator.comparingInt(e -> e.getValue().size());
    Comparator<Map.Entry<String, List<String>>> keyComparison = Comparator.comparing(Map.Entry::getKey);

    Map<String, List<String>> getLangsMapSortedByNumOfProgs() {
        Map<String, List<String>> result = sorted(programmers, valueComparison.reversed().thenComparing(keyComparison = Comparator.comparing(Map.Entry::getKey)));
        return result;
    }

    Map<String, List<String>> getProgsMapSortedByNumOfLangs() {
        Map<String, List<String>> result = sorted(getProgsMap(), valueComparison.reversed().thenComparing(keyComparison));
        return result;
    }

    Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int i) {
        Map <String, List<String>> result = filtered(getProgsMap(), numb -> numb.getValue().size() > i);
        return result;
    }

    protected Map<String, List<String>> sorted(Map<String, List<String>> mapToBeSorted, Comparator<Map.Entry<String, List<String>>> comp) {
        Map<String, List<String>> result = mapToBeSorted.entrySet().stream().sorted(comp)
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(l, lst) -> l, LinkedHashMap::new));
        return result;
    }

    protected Map <String, List<String>> filtered(Map<String, List<String>> mapToBeFiltered, Predicate<Map.Entry<String, List<String>>> pred) {
        Map<String, List<String>> result =  mapToBeFiltered.entrySet().stream()
                .filter(pred).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (l, lst) -> l, LinkedHashMap::new));
        return result;
    }
}