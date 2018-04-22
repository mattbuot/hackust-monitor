import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import History.Entry;

public final class BlackList {

    public static long limit;
    public static List<String> websites = new ArrayList<>();

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

        limit = Long.parseLong(list.get(0));

        for (int i = 1; i < list.size(); i++) {
            websites.add(list.get(i));
        }
    }
    
    public static void checkTime(List<Entry> history) {
    	long sum = 0;
    	for(String site: BlackList.websites) {
    	    long time = Entry.totalVisitTime(history, site) / 1000;
    	    System.out.println("You spent " + time + "s on " + site);
    	    sum += time;
        }

        if(sum >= BlackList.limit) {
    	    System.out.println("Limit reached!!!!");
        }

    }
}
