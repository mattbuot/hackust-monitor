import History.Entry;

import java.io.PrintWriter;
import java.util.List;

public class Output {

    public static void writeUsageStats(String path, List<Entry> data, long limit) {
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println(limit);
        for(int i = 0; i < data.size(); i++){
            writer.println();
        }
        writer.close();
    }
}
