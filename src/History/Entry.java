package History;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class Entry {
	
	private String url;
	private String title;
	private int numVisit;
	private Date date;
	private long visitDuration;
	
	public Entry(String url, String title, int numVisit, Date date, long visitDuration) {
		this.url = url;
		this.title = title;
		this.numVisit = numVisit;
		this.date = date;
		this.visitDuration = visitDuration;
	}
	
	public Entry(String url, String title, int numVisit, String dateStr, int visitDuration) {
		this(url,title,numVisit,formatDate(dateStr),visitDuration);
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

	public long getVisitDuration() {
		return visitDuration;
	}

	public Date getDate() {
		return date;
	}
	
	public Entry withVisitDuration(long visitDuration) {
		return new Entry(this.url, this.title, this.numVisit, this.date, visitDuration);
	}
	
	@Override
	public String toString() {
		return url +  "\t" + title + "\t"+ numVisit + "\t" + date.toString() + "\t";
	}
	
	public static List<Entry> computeVisitTime(List<Entry> history) {
		List<Entry> updatedHistory = new ArrayList<>();
		
		Collections.sort(history, Comparator.comparing(Entry::getDate));
		history = removeDuplicates(history);
		for(int index = 0; index < history.size(); index++) {
			Entry nextEntry = history.get(index);
			//System.out.println(nextEntry.title + ", " + nextEntry.getDate());
			if((nextEntry.getVisitDuration() == 0)) {
				long startTime = nextEntry.getDate().getTime();
				//System.out.println("Start: " + startTime);
				long endTime;
				if(index == (history.size()-1)) {
					endTime = System.currentTimeMillis();
				} else {
					endTime = history.get(index+1).getDate().getTime();
				}
				//System.out.println("End: " + endTime);
				if(index == (history.size()-1)) {
					endTime = System.currentTimeMillis();
				}
				//System.out.println("website, End - Start = " + nextEntry.getTitle() + " " + (endTime-startTime));
				nextEntry = nextEntry.withVisitDuration(endTime-startTime);
			} 
			//System.out.println(nextEntry.getTitle() + ", " + nextEntry.getVisitDuration());
			updatedHistory.add(nextEntry);
		}
		
		return updatedHistory;
	}

	public static long totalVisitTime(List<Entry> completeHistory, String website) {
		long sum = completeHistory.stream()
								  .filter(e -> e.getURL().contains(website))
								  .mapToLong(Entry::getVisitDuration)
								  .sum();

		return sum;
	}
	
	private static List<Entry> removeDuplicates(List<Entry> history) {
		List<Entry> updatedHistory = new ArrayList<>();
		
		if(!history.isEmpty()) {
			Iterator<Entry> iter = history.iterator();
			Entry last = iter.next();
			while(iter.hasNext()) {
				Entry next = iter.next();
				if(!last.getDate().equals(next.getDate())) {
					updatedHistory.add(last);
				}
				last = next;
			}
			updatedHistory.add(last);
		}
		
		return updatedHistory;
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
	
	public static Date formatDate(String dateStr) {
		String[] dateTime = dateStr.split(" ");
		String dateform = dateTime[0] + "T" + dateTime[1] + "+0800";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			return formatter.parse(dateform);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
 