package zad1;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;

public class CustomFileVisitor
	extends SimpleFileVisitor<Path>{
	
	private FileChannel outputFileChannel;
	private ByteBuffer buffer;
	private Charset inputCharset  = Charset.forName("Cp1250"),
	            	outputCharset = Charset.forName("UTF-8");
	
	public CustomFileVisitor(Path outputPath) {
		try {
			this.outputFileChannel = FileChannel.open(outputPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
	        System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}
	private void codeToUtf (FileChannel inputChannel, long bufferSize) {
		buffer = ByteBuffer.allocate((int) (bufferSize));
		try {
			inputChannel.read(buffer);
			buffer.flip();
			
			CharBuffer charBuffer = inputCharset.decode(buffer);
			ByteBuffer convertedBuffer =
					outputCharset.encode(charBuffer);
			while(convertedBuffer.hasRemaining()) {
				outputFileChannel.write(convertedBuffer);
			}
			
		} catch (IOException e) {
	        System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
        if (attr.isSymbolicLink()) {
            System.out.format("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
            System.out.format("Regular file: %s ", file);
        } else {
            System.out.format("Other: %s ", file);
        }
        try {
			codeToUtf(FileChannel.open(file), attr.size());
		} catch (IOException e) {
	        System.out.println(e.toString());
			e.printStackTrace();
		}
        
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                          IOException exc) {
        System.out.format("Directory: %s%n", dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                       IOException exc) {
        System.err.println(exc);
        return FileVisitResult.CONTINUE;
    }
}
