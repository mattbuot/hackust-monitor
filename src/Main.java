import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
    
    public static ArrayList<Entry> fetchHistory(){
        String sql = "SELECT * FROM urls";
        ArrayList<Entry> entries = new ArrayList<>();
        
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	Entry nextEntry = new Entry(rs.getString("url"),rs.getString("title"),rs.getInt("visit_count"));
            	entries.add(nextEntry);
            }
            return entries;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
    	String historyPath = args[0];
    	String str = NAVIGATION_HISTORY + historyPath;
    	System.out.println(str);
    	connect(NAVIGATION_HISTORY + historyPath);
    	ArrayList<Entry> entries = fetchHistory();
    	for(Entry nextEntry: entries) {
    		System.out.println(nextEntry);
    	}
    	close();
    }
}
