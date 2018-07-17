package com.it.soul.lab.service.models;

import java.io.Serializable;
import java.util.List;

import com.it.soul.lab.sql.query.models.Property;

public class FetchQuery implements Serializable {

	private static final long serialVersionUID = 2000111050307543750L;
	private String table;
	private String orderBy;
	private Integer location;
	private Integer size;
	private List<Criteria> criterias;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public Integer getLocation() {
		return location;
	}
	public Integer getOffset() {
		return getLocation();
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getSize() {
		return size;
	}
	public Integer getLimit() {
		return getSize();
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public List<Criteria> getCriterias() {
		return criterias;
	}
	public void setCriterias(List<Criteria> criterias) {
		this.criterias = criterias;
	}

	
}
