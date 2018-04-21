import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import History.Config;
import History.Entry;

public class Main {

	// ask the user to enter location
    private static final String NAVIGATION_HISTORY = "jdbc:sqlite:";
    private static Connection conn = null;

    public static void connect(String path) {
        try {

            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(path);

            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    public static void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static List<Entry> fetchHistory(){
        String sql = "SELECT cast(datetime((last_visit_time + 8 * 60 * 60 * 1000 * 1000)/ 1000000 + (strftime('%s', '1601-01-01')), 'unixepoch') as VARCHAR(16)) as last_visited, " + 
        		"  urls.url, title, visit_count, visit_duration FROM urls INNER JOIN visits ON urls.id = visits.url";
        List<Entry> entries = new ArrayList<>();
        
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	Entry nextEntry = new Entry(rs.getString("url"),rs.getString("title"),
                        rs.getInt("visit_count"), rs.getString("last_visited"), rs.getInt("visit_duration")/1000);
            	entries.add(nextEntry);
            }
            return entries;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void copyFiles(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) throws IOException {
    	String historyPath = args[0];
    	copyFiles(new File(historyPath), new File(historyPath + "2"));
    	connect(NAVIGATION_HISTORY + historyPath + "2");
    	List<Entry> history = fetchHistory();
    	Config.loadFile("src/config.txt");
    	System.out.println("Start (ms): " + Config.start);
    	history = Entry.computeVisitTime(history);
    	history = Entry.filterDates(history, Config.start, Config.end);

    	BlackList.loadFile("src/blacklist.txt");
    	System.out.println("Time spent on Facebook: " + Entry.totalVisitTime(history, "facebook.com")/1000 + "s");
    	System.out.println("Blacklist limit: " + BlackList.limit + " length: " + (BlackList.websites.size() - 1));

    	long sum = 0;
    	for(String site: BlackList.websites) {
    	    long time = Entry.totalVisitTime(history, site) / 1000;
    	    System.out.println("You spent " + time + "s on " + site);
    	    sum += time;
        }

        if(sum >= BlackList.limit) {
    	    System.out.println("Limit reached!!!!");
        }
    	close();
    }
}
