package zad1;


import java.util.ArrayList;
import java.util.List;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;

import static java.nio.file.FileVisitResult.CONTINUE;
public class Futil {

    public static void processDir(String dir, String resultFileName) {
        List<String> list = new ArrayList<>();

        try {
            SimpleFileVisitor fileVisitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attr){
                    BufferedReader bf = null;

                    try {
                    	bf = Files.newBufferedReader(path, Charset.forName("windows-1250"));
                        String line;
                        while ((line = bf.readLine()) != null)
                        	list.add(line);

                        Files.write(new File(resultFileName).toPath(), list, Charset.forName("UTF8"));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return CONTINUE;
                }
            };


            Files.walkFileTree(Paths.get(dir), fileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
