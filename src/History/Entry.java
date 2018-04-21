package History;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class Entry {
	
	private String url;
	private String title;
	private int numVisit;
	private Date date;
	private int visitDuration;
	
	public Entry(String url, String title, int numVisit, String date, int visitDuration) {
		this.url = url;
		this.title = title;
		this.numVisit = numVisit;
		this.visitDuration = visitDuration;
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

	public int getVisitDuration() {
		return visitDuration;
	}

	public Date getDate() {
		return date;
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

	public static double computeVisitTime(List<Entry> history, String website) {
		Collections.sort(history, Comparator.comparing(Entry::getDate));
		history = filterUrls(history, website);

		double sum = 0;

		for(int i = 0; i < history.size(); i++) {
			int duration;

			//FIRST CASE: chrome history has stored the visit duration
			if((duration = history.get(i).getVisitDuration()) > 0) {
				sum += (double) duration;
			}
		}

		return sum;

	}
}
