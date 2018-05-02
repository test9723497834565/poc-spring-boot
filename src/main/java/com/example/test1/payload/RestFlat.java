package com.example.test1.payload;

import java.util.Date;


public class RestFlat {
	
	private String name;
	private int age;
	private Date date;
	
	public RestFlat() {
		// necessary for test
	}
	
	public RestFlat(String name, int age, Date date) {
		this.name = name;
		this.age = age;
		this.date = date;
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
	
	
	
}
