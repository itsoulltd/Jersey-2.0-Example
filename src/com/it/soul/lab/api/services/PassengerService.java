package com.it.soul.lab.api.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.it.soul.lab.connect.JDBConnectionPool;
import com.it.soul.lab.jersey.example.app.JPAResourceLoader;
import com.it.soul.lab.jersey.example.app.SecuredAuthorization;
import com.it.soul.lab.service.ORMService;
import com.it.soul.lab.service.jpa.models.JPAPassenger;
import com.it.soul.lab.service.models.Criteria;
import com.it.soul.lab.service.models.FetchQuery;
import com.it.soul.lab.service.models.Passenger;
import com.it.soul.lab.service.models.PassengerList;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.query.SQLQuery;
import com.it.soul.lab.sql.query.SQLQuery.QueryType;
import com.it.soul.lab.sql.query.SQLSelectQuery;
import com.it.soul.lab.sql.query.models.AndExpression;
import com.it.soul.lab.sql.query.models.DataType;
import com.it.soul.lab.sql.query.models.Expression;
import com.it.soul.lab.sql.query.models.ExpressionInterpreter;
import com.it.soul.lab.sql.query.models.Operator;
import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;
import com.it.soul.lab.sql.query.models.Table;

@Path("/passenger")
public class PassengerService {

	@GET @Path("/{location}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPassengerAt(@PathParam("location") Integer offset, @PathParam("size") Integer limit) {
		String error = "";
		try {
			Connection conn = JDBConnectionPool.connection("testDB");
			SQLExecutor exe = new SQLExecutor(conn);
			
			SQLSelectQuery query = (SQLSelectQuery) new SQLQuery.Builder(QueryType.SELECT)
					.columns()
					.from("Passenger")
					.orderBy("id")
					.addLimit(limit, offset)
					.build();
			ResultSet set = exe.executeSelect(query);
			Table items = exe.collection(set);
			exe = null;//exe.close(); //automatically called when executor object is garbage collected.
			
			PassengerList list = new PassengerList();
			for (Row item : items.getRows()) {
				list.add(item.inflate(Passenger.class));
			}
			
			return Response.status(200).entity(list).build();
			
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	@GET @SecuredAuthorization @Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response allPassgenger() {
		String error = "";
		try {
			Connection conn = JDBConnectionPool.connection("testDB");
			SQLExecutor exe = new SQLExecutor(conn);
			
			SQLSelectQuery query = (SQLSelectQuery) new SQLQuery.Builder(QueryType.SELECT)
					.columns()
					.from("Passenger")
					.orderBy("age")
					.build();
			ResultSet set = exe.executeSelect(query);
			Table items = exe.collection(set);
			exe = null;//exe.close(); //automatically called when executor object is garbage collected.
			
			PassengerList list = new PassengerList();
			for (Row item : items.getRows()) {
				list.add(new Passenger(item));
			}
			
			return Response.status(200).entity(list).build();
			
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	@GET @Path("JPA/{location}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPassengerUsingJPA(@PathParam("location") Integer offset, @PathParam("size") Integer limit) {
		String error = "";
		try {
			ORMService<JPAPassenger> allPass = new ORMService<>(JPAResourceLoader.entityManager(), JPAPassenger.class);
			List<JPAPassenger> passengers = (List<JPAPassenger>) allPass.findAll();
			PassengerList list = new PassengerList();
			for (JPAPassenger item : passengers) {
				list.add(new Passenger(item));
			}
			return Response.status(200).entity(list).build();
			
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	@GET @Path("JPA/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPassengerUsingJPAByID(@PathParam("id") Integer id) {
		String error = "";
		try {
			ORMService<JPAPassenger> allPass = new ORMService<>(JPAResourceLoader.entityManager(), JPAPassenger.class);
			JPAPassenger passenger = (JPAPassenger) allPass.findBy(new Property("id", id, DataType.INT));
			return Response.status(200).entity(passenger).build();
			
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	@POST @Path("JPA")
	@Produces(MediaType.APPLICATION_JSON) 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetch(FetchQuery fquery) {
		String error = "";
		try {
			ORMService<JPAPassenger> service = new ORMService<>(JPAResourceLoader.entityManager(), JPAPassenger.class);
			ExpressionInterpreter and = null;
			ExpressionInterpreter lhr = null;
			for (Criteria element : fquery.getCriterias()) {
				Property prop = element.getProperty();
				if(lhr == null) {
					lhr = new Expression(prop, Operator.EQUAL);
					and = lhr;
				}else {
					ExpressionInterpreter rhr = new Expression(prop, Operator.EQUAL);
					and = new AndExpression(lhr, rhr);
					lhr = and;
				}
			}
			List<JPAPassenger> items = (List<JPAPassenger>) service.findMatches(and);
			PassengerList list = new PassengerList();
			for (JPAPassenger item : items) {
				list.add(new Passenger(item));
			}
			return Response.status(200).entity(list).build();
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
}
