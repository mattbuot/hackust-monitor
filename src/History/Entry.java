package History;

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
}
