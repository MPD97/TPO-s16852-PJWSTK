/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad4;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {


        List<String> listWords = new ArrayList<>();
        Map<String, List<String>> wordsAnagrams = new HashMap<>();

        try {
            Scanner scan = new Scanner(new URL("http://wiki.puzzlers.org/pub/wordlists/unixdict.txt").openStream());
            while (scan.useDelimiter("\\A").hasNextLine()) {
            	listWords.add(scan.nextLine());
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        };
        Stream<String> list = listWords.stream();
        list.sorted()
                .collect(Collectors.groupingBy(s -> {
                    char[] charTab = s.toCharArray();
                    Arrays.sort(charTab);
                    return String.valueOf(charTab);
                },
                	Collectors.mapping(m -> m, Collectors.toList())))
                .values().stream()
                .filter(string -> string.size() >= 2)
                .sorted((x, y) -> y.size() - x.size())
                .map(str -> str.stream().collect(Collectors.toMap(m -> m, s -> str)))
                .forEach(strList -> strList.forEach(wordsAnagrams::put));

        wordsAnagrams
                .forEach((output, str) -> {
                            List<String> outputList = str.stream().filter(a -> !(a.equals(output))).collect(Collectors.toList());
                            String printedResult = output + " " + outputList.stream().reduce((a, b) -> a + " " + b).get();
                            System.out.println(printedResult);
                        }
                );
    }
}
