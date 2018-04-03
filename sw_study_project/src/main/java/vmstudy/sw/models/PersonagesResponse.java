package vmstudy.sw.models;

import java.util.List;

public class PersonagesResponse {

	private int count;
	private String next;
	private String previous;
	private List<Personage> results;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getPrevious() {
		return previous;
	}
	public void setPrevious(String previous) {
		this.previous = previous;
	}
	public List<Personage> getResults() {
		return results;
	}
	public void setResults(List<Personage> results) {
		this.results = results;
	}
}
