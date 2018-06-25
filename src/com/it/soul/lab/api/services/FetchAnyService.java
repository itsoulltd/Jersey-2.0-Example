package com.it.soul.lab.api.services;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.it.soul.lab.connect.JDBConnectionPool;
import com.it.soul.lab.service.models.FetchQuery;
import com.it.soul.lab.service.models.Passenger;
import com.it.soul.lab.service.models.PassengerList;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.query.SQLQuery;
import com.it.soul.lab.sql.query.SQLSelectQuery;
import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;
import com.it.soul.lab.sql.query.models.Table;
import com.it.soul.lab.sql.query.SQLQuery.QueryType;

@Path("/fetch")
public class FetchAnyService {
	
	@GET @Path("/{table}/{orderBy}/{location}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetch(@PathParam("table") String table
			, @PathParam("orderBy") String orderBy
			, @PathParam("size") Integer limit
			, @PathParam("location") Integer offset) {
		String error = "";
		try {
			Connection conn = JDBConnectionPool.shared().getConnectionFromPool("testDB");
			SQLExecutor exe = new SQLExecutor(conn);
			
			SQLSelectQuery query = (SQLSelectQuery) new SQLQuery.Builder(QueryType.SELECT)
					.columns()
					.from(table)
					.orderBy(orderBy)
					.addLimit(limit, offset)
					.build();
			ResultSet set = exe.executeSelect(query);
			Table items = exe.collection(set);
			exe = null;//exe.close(); //automatically called when executor object is garbage collected.
			
			if(table.toLowerCase().equals("passenger")) {
				PassengerList list = new PassengerList();
				for(Row row : items.getRows()) {
					Passenger pass = row.inflate(Passenger.class);
					list.add(null).add(pass);
				}
				return Response.status(200).entity(list).build();
			}else {
				return Response.status(200).entity(items).build();
			}
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	@POST 
	@Produces(MediaType.APPLICATION_JSON) 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetch(FetchQuery fquery) {
		String error = "";
		try {
			Connection conn = JDBConnectionPool.shared().getConnectionFromPool("testDB");
			SQLExecutor exe = new SQLExecutor(conn);
			
			SQLSelectQuery query = (SQLSelectQuery) new SQLQuery.Builder(QueryType.SELECT)
					.columns()
					.from(fquery.getTable())
					.orderBy(fquery.getOrderBy())
					.addLimit(fquery.getLimit(), fquery.getOffset())
					.build();
			ResultSet set = exe.executeSelect(query);
			Table items = exe.collection(set);
			exe = null;//exe.close(); //automatically called when executor object is garbage collected.
			
			if(fquery.getTable().toLowerCase().equals("passenger")) {
				PassengerList list = new PassengerList();
				for(Row row : items.getRows()) {
					Passenger pass = row.inflate(Passenger.class);
					list.add(null).add(pass);
				}
				return Response.status(200).entity(list).build();
			}else {
				return Response.status(200).entity(items).build();
			}
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	/**
	 * 
	 * @param row
	 * @param type
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unused")
	private <T> T convert(Row row, Class<T> type) throws InstantiationException, IllegalAccessException {
		Class<T> cls = type;
		T newInstance = cls.newInstance();
		Field[] fields = cls.getDeclaredFields();
		Map<String, Property> data = row.keyValueMap();
        for (Field field : fields) {
            field.setAccessible(true);
            Property entry = data.get(field.getName());
            if(entry != null) {
            	field.set(newInstance, entry.getValue());
            }
        }
		return newInstance;
	}
	
}
