package zad2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class Futil {

    @FunctionalInterface
    public interface ExceptionFunction<S, T> extends Function<S, T> {

        @Override
        default T apply(S s) {
            try {
                return applyException(s);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        T applyException(S s) throws IOException;
    }

    public static ExceptionFunction<Path, BufferedReader> BUFFERED_READER_CONTENT = path -> 
    	Files.newBufferedReader(path, Charset.forName("windows-1250"));


    public static void processDir(String path, String result) {

        try {
            Stream<String> filesStream = Files.walk(Paths.get(path))
                    .map(BUFFERED_READER_CONTENT)
                    .map(BufferedReader::lines)
                    .map(readLines -> readLines.collect(Collectors.toList()))
                    .flatMap(Collection::stream);

            List<String> collectedContent = filesStream.collect(Collectors.toList());

            Files.write(new File(result).toPath(), collectedContent, Charset.forName("utf8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}