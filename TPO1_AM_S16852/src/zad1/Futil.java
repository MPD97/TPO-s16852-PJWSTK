package zad1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Futil {
	
	public static void processDir(String dirName,
			String resultFileName) {

		CustomFileVisitor visitor = new CustomFileVisitor(Paths.get(resultFileName));
		try {
			Files.walkFileTree(Paths.get(dirName), visitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
