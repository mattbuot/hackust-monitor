import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static final String NAVIGATION_HISTORY = "";

    public static void main(String[] args) throws IOException {

        System.out.println("Hello World!");

        String inputPath = NAVIGATION_HISTORY;

        String line;
        BufferedReader reader = new BufferedReader(new FileReader(inputPath));
        try {
            line = reader.readLine();
            while (line != null) {
                loadLine(String line);
            }
        } finally {
            reader.close();
        }

    }

    private static void loadLine(String line) {

    }
}
