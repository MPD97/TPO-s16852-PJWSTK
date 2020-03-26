/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad3;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Anagrams {
	    List<String> resultEntry = new ArrayList<>();
	    List<String> resultInput = new ArrayList<>();
	    List<List<String>> resultOutput = new ArrayList<>();

	    public Anagrams(String input) {

	        try {
	            List<String> ListLines = Files.readAllLines(Paths.get(input));

	            for (String line : ListLines)
	            	resultEntry.add(line);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        for (String s : resultEntry)
	        	resultInput.addAll(Arrays.asList(s.split(" ")));
	    }
	    public List<List<String>> getSortedByAnQty() {
	        Function<String, String> sortFunc = s -> {
                  char [] chars = s.toCharArray();
                  Arrays.sort(chars);
                  return String.valueOf(chars);
                 };
	        Map<String, List<String>> sortingResult = resultInput.stream()
	                .collect(Collectors.groupingBy(sortFunc, Collectors.mapping(s -> s, Collectors.toList())));

	        resultOutput = new ArrayList<>(sortingResult.values());
            resultOutput.sort((s1, s2) -> s2.size() - s1.size());
            
	        return resultOutput;
	    }
	    public String getAnagramsFor(String word) {
	        String matchedResults = word + ": [";

	        matchedResults += resultOutput.stream().filter(s -> s.contains(word))
	                .flatMap(Collection::stream)
	                .filter(s -> !(s.equals(word)))
	                .collect(Collectors.joining(", "));

	        matchedResults += "]";
	        return matchedResults;
	    }
}  
