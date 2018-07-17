package com.it.soul.lab.service.models;

import com.it.soul.lab.sql.query.models.DataType;
import com.it.soul.lab.sql.query.models.Property;

public class Criteria {
	private String key;
	private String value;
	private String type;
	public Criteria() {
		super();
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Property getProperty() {
		Property prop = new Property(getKey());
		//TODO: All DataType Implementations.
		if(getType().trim().equalsIgnoreCase(DataType.INT.toString())) {
			prop.setType(DataType.INT);
			prop.setValue(Integer.valueOf(getValue()));
		}else {
			prop.setType(DataType.STRING);
			prop.setValue(getValue());
		}
		return prop;
	}
}
