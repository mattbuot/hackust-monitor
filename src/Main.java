import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	// ask the user to enter location
    private static final String NAVIGATION_HISTORY = "/home/julien/.config/google-chrome/Default/History";
    
    public static void connect(String path) {
        Connection conn = null;
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(path);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
    	connect(NAVIGATION_HISTORY);
    	
    }
}
