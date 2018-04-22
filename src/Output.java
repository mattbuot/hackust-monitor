import History.Entry;
import History.URL_Util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Output {

    public static final String USAGE_PATH = "out/usage_stats.txt";
    public static final String CATEGORIES_PATH = "out/categories_stats.txt";
    public static final String YT_PATH = "out/yt_stats.txt";



    public static void writeUsageStats(String path, List<Entry> data) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        long total = 0L;

        Map<String, Long> totals = new HashMap<>();

        for(Entry entry: data){
            for(String website: BlackList.websites) {
                if(entry.getURL().contains(website)) {
                    totals.put(website, totals.getOrDefault(website, 0L) + entry.getVisitDuration() / 1000);
                    total += entry.getVisitDuration() / 1000;
                }
            }
        }

        for(Map.Entry<String, Long> entry: totals.entrySet()) {
            writer.println(entry.getKey() + " " + entry.getValue());
        }

        if(total < BlackList.limit) {
            writer.println("Free time " + (BlackList.limit - total));
        }
        writer.close();
    }

    public static void writeCategoriesStats(String path, List<Entry> data) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        Map<String, List<Entry>> entriesPerCategory = data.stream().filter(e -> e.getCategory() != null).collect(Collectors.groupingBy(Entry::getCategory));

        for(Map.Entry<String, List<Entry>> e: entriesPerCategory.entrySet()){
            writer.print(e.getKey() + " ");
            writer.println(e.getValue().stream().mapToLong(entry -> entry.getVisitDuration() / 1000).sum());
        }
        writer.close();
    }

    public static void writeYTCategoriesStats(String path, List<Entry> data) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        Map<String, List<Entry>> entriesPerCategory = data.stream().filter(e -> URL_Util.getYTCategory(e.getURL()) != null).collect(Collectors.groupingBy(x -> URL_Util.getYTCategory(x.getURL())));

        for(Map.Entry<String, List<Entry>> e: entriesPerCategory.entrySet()){
            writer.print(e.getKey() + " ");
            writer.println(e.getValue().stream().mapToLong(entry -> entry.getVisitDuration() / 1000).sum());
        }
        writer.close();
    }
}
