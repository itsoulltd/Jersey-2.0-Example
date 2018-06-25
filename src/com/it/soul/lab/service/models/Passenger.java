package com.it.soul.lab.service.models;

import java.util.Map;

import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;

public class Passenger {
	private String name;
	private Integer id;
	private Integer age;
	private String sex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Passenger() {}
	
	public Passenger(Map<String,Property> properties) {
		name = (String)((Property)properties.get("name")).getValue();
		id =  (Integer)((Property)properties.get("id")).getValue();
		age = (Integer)((Property)properties.get("age")).getValue();
		sex = (String)((Property)properties.get("sex")).getValue();
	}
	
	public Passenger(Row properties) {
		this(properties.keyValueMap());
	}
	
}
