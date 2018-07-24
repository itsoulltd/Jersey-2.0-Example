package com.itsoul.lab.domains;

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
		if(getType().trim().equalsIgnoreCase(DataType.INT.toString())) {
			prop.setType(DataType.INT);
			prop.setValue(Integer.valueOf(getValue()));
		}
		else if(getType().trim().equalsIgnoreCase(DataType.BOOL.toString())) {
			prop.setType(DataType.BOOL);
			prop.setValue(Boolean.valueOf(getValue()));
		}
		else if(getType().trim().equalsIgnoreCase(DataType.DOUBLE.toString())
				|| getType().trim().equalsIgnoreCase(DataType.FLOAT.toString())) {
			prop.setType(DataType.DOUBLE);
			prop.setValue(Double.valueOf(getValue()));
		}
		else {
			prop.setType(DataType.STRING);
			prop.setValue(getValue());
		}
		return prop;
	}
	public Criteria(String key, String value, DataType type) {
		super();
		this.key = key;
		this.value = value;
		this.type = type.toString();
	}
}
