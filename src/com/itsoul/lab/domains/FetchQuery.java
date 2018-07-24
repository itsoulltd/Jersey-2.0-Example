package com.itsoul.lab.domains;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.it.soul.lab.sql.query.models.DataType;

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
	public void addCriteria(String key, String value, DataType type) {
		if(getCriterias() == null) {
			List<Criteria> criterias = new ArrayList<>();
			setCriterias(criterias);
		}
		getCriterias().add(new Criteria(key, value, type));
	}
	
}
