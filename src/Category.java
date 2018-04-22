import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Category {

    private static Map<String, String> categories;
    private static final String CATEGORIES_PATH = "src/categories.txt";

    public static Map<String, String> getCategories() throws IOException {
        if(categories == null) {
            loadFile(CATEGORIES_PATH);
        }
        return new HashMap<>(categories);
    }

    public static void loadFile(String path) throws IOException {
        categories = new HashMap<>();
        List<String> list = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        try {
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
        } finally {
            reader.close();
        }

        for(int i = 0; i < list.size(); i++) {
            if(!list.get(i).isEmpty()) {
                String[] record = list.get(i).split(" , ");
                categories.put(record[0], record[1]);
            }
        }
    }
}
