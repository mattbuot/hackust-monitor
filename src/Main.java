import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.Group;
 

import History.Entry;

public class Main extends Application {

	// ask the user to enter location
    private static final String NAVIGATION_HISTORY = "jdbc:sqlite:";
    private static Connection conn = null;
    private static String historyPath;
    private static ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

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

            Map<String, String> categories = Category.getCategories();

            // loop through the result set
            while (rs.next()) {

            	Entry nextEntry = new Entry(rs.getString("url"),rs.getString("title"),
                        rs.getInt("visit_count"), rs.getString("last_visited"), rs.getInt("visit_duration")/1000, Entry.findCategory(categories, rs.getString("url")));
            	entries.add(nextEntry);
            }
            return entries;
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void copyFiles(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    
    private static void updateData(String site, long visitTime)
    {
        for(Data d : pieChartData) {
            if(d.getName().equals(site))
            {
                d.setPieValue(visitTime);
                return;
            }
        }
        pieChartData.add(new PieChart.Data(site, visitTime));
    }

    
    @Override 
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Blacklist");
        stage.setWidth(500);
        stage.setHeight(500);
        
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Blacklist");

        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        
        stage.show();
    }
    
    
    @SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
    	historyPath = args[0];

    	BlackList.loadFile("src/blacklist.txt");
        Config.loadFile("src/config.txt");
        
        
    	copyFiles(new File(historyPath), new File(historyPath + "2"));
		
    	connect(NAVIGATION_HISTORY + historyPath + "2");
    	
        List<Entry> history = fetchHistory();
    	close();
    	
        history = Entry.computeVisitTime(history);
        history = Config.filterDates(history, Config.start, Config.end);
    	BlackList.checkTime(history);
    	
    	for(String site: BlackList.websites) {
        	long visitTime = Entry.totalVisitTime(history, site);
        	updateData(site,visitTime);
        }

    	
		Output.writeUsageStats(Output.USAGE_PATH, history);
		Output.writeCategoriesStats(Output.CATEGORIES_PATH, history);
    	Output.writeYTCategoriesStats(Output.YT_PATH, history);
        
        launch(args);
    }
}
