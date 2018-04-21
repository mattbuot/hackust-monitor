import History.Entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Config {

    public static Date start;
    public static Date end;

    public static void loadFile(String path) throws IOException {

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

        start = Entry.formatDate(list.get(0));
        end = Entry.formatDate(list.get(1));
    }

    public static List<Entry> filterDates(List<Entry> history, Date begin, Date end) {
        List<Entry> filtered = new ArrayList<>();

        for(Entry e: history) {
            if((e.getDate().compareTo(Config.start)) > 0 && (e.getDate().compareTo(Config.end)  < 0)) {
                filtered.add(e);
            }
        }

        return filtered;
    }
}
