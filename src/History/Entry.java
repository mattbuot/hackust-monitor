package History;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Entry {
	
	private String url;
	private String title;
	private int numVisit;
	private Date date;
	
	public Entry(String url, String title, int numVisit, String date) {
		this.url = url;
		this.title = title;
		this.numVisit = numVisit;
		String[] dateTime = date.split(" ");
		String dateform = dateTime[0] + "T" + dateTime[1] + "+0800";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			this.date = formatter.parse(dateform);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getURL() {
		return url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getNumVisit() {
		return numVisit;
	}
	
	@Override
	public String toString() {
		return url +  "\t" + title + "\t"+ numVisit + "\t" + date.toString() + "\t";
	}

	public static List<Entry> filterUrls(List<Entry> entries, String filter){
		List<Entry> filteredList = new ArrayList<>();

		for(Entry e: entries){
			if(e.getURL().contains(filter)){
				filteredList.add(e);
			}
		}
		return filteredList;
	}
	}
