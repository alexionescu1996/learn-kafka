import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {


    public static void main(String[] args) throws IOException {

        String path = "src/main/resources/test.txt";

        readingRawBytesBuffer(path);

        readingLineByLine(path);

        writeToFile(path);

        try (Stream<String> lines = Files.lines(Path.of(path))) {
            lines.forEach(System.out::println);
        }

    }

    private static void writeToFile(String path) throws IOException {
        try (InputStream is = new FileInputStream(path);
             OutputStream os = new FileOutputStream("src/main/resources/test-copy.txt")) {

            long total = 0;
            byte[] buffer = new byte[64 * 1024];
            int bytesRead;

//            How many bytes did we just read into the buffer?
//            -1 EOF
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                total += bytesRead;
            }
            System.out.println("done writing " + total + " bytes");
            os.flush();
        }
    }

    private static void readingLineByLine(String path) throws IOException {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                System.out.println("Line " + (++count) + " " + line);
            }

            System.out.println("done");
        }
    }


    private static void readingRawBytesBuffer(String path) throws IOException {
        byte[] buffer = new byte[64 * 1024];

        try (InputStream is = new FileInputStream(path)) {
            int bytesRead;
            long totalRead = 0;

            while ((bytesRead = is.read(buffer)) != -1) {
                System.out.println("Read " + bytesRead + " bytes");
                totalRead += bytesRead;
            }

            System.out.println("Finished reading. Total: " + totalRead + " bytes");
        }
    }
}
