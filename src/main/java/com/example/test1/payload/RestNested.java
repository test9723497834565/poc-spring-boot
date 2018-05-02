package com.example.test1.payload;

import java.util.Date;
import java.util.List;

public class RestNested {

	private String name;
	private int age;
	private Date date;
	
	private List<String> listString;
	
	private List<RestFlat> listFlat;
	
	private List<RestNested> listRestNested;

	public RestNested() {
		
	}
			
	public RestNested(String name, int age, Date date, List<String> listString, List<RestFlat> listFlat,
			List<RestNested> listRestNested) {
		this.name = name;
		this.age = age;
		this.date = date;
		this.listString = listString;
		this.listFlat = listFlat;
		this.listRestNested = listRestNested;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getListString() {
		return listString;
	}

	public void setListString(List<String> listString) {
		this.listString = listString;
	}

	public List<RestFlat> getListFlat() {
		return listFlat;
	}

	public void setListFlat(List<RestFlat> listFlat) {
		this.listFlat = listFlat;
	}

	public List<RestNested> getListRestNested() {
		return listRestNested;
	}

	public void setListRestNested(List<RestNested> listRestNested) {
		this.listRestNested = listRestNested;
	}

	
	
}
