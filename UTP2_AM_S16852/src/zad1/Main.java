/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Main {
  public static void main(String[] args) {
    
      Function<String, List<String>> flines = fname -> {
          List<String> lines = new ArrayList<>();
          try {
              BufferedReader b = new BufferedReader(new FileReader(fname));
              String readLine = "";
              while ((readLine = b.readLine()) != null) {
            	  lines.add(readLine);
              }
          } catch (Exception ex) {
              ex.printStackTrace();
          }
          return lines;
      };
      Function<List<String>, String> join = list -> {
          String joinOutput = "";

          for (String item : list) {
              joinOutput += item;
          }
          String result = joinOutput.replaceAll("\\s+ ", ""); 
          return result;
      };
      Function<String, List<Integer>> collectInts = column -> {
          String[] list = column.replaceAll("[^0-9]+", " ").split(" ");
          List<Integer> result = new ArrayList<>();
          for (String s : list) {
              if (s.matches("[0-9]+")) {  
            	  result.add(Integer.parseInt(s));
              }
          }
          return result;
      };
      Function<List<Integer>, Integer> sum = collectedInts -> {
          int sumOutput = 0;

          for (Integer i : collectedInts) {
              sumOutput += i;
          }
          return sumOutput;
      };

    String fname = System.getProperty("user.home") + "/LamComFile.txt"; 
    InputConverter<String> fileConv = new InputConverter<>(fname);
    List<String> lines = fileConv.convertBy(flines);
    String text = fileConv.convertBy(flines, join);
    List<Integer> ints = fileConv.convertBy(flines, join, collectInts);
    Integer sumints = fileConv.convertBy(flines, join, collectInts, sum);

    System.out.println(lines);
    System.out.println(text);
    System.out.println(ints);
    System.out.println(sumints);

    List<String> arglist = Arrays.asList(args);
    InputConverter<List<String>> slistConv = new InputConverter<>(arglist);  
    sumints = slistConv.convertBy(join, collectInts, sum);
    System.out.println(sumints);

  }
}
