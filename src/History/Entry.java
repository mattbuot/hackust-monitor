package History;

import java.util.ArrayList;
import java.util.List;

public final class Entry {
	
	private String url;
	private String title;
	private int numVisit;
	
	public Entry(String url, String title, int numVisit) {
		this.url = url;
		this.title = title;
		this.numVisit = numVisit;
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
		return url +  "\t" + title + "\t"+ numVisit + "\t";
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
