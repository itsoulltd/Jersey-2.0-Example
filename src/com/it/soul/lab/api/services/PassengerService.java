package com.it.soul.lab.api.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import javax.persistence.Query;
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
import com.it.soul.lab.jpql.query.JPQLQuery;
import com.it.soul.lab.jpql.query.JPQLSelectQuery;
import com.it.soul.lab.service.ORMService;
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
import com.itsoul.lab.domains.Criteria;
import com.itsoul.lab.domains.FetchQuery;
import com.itsoul.lab.domains.Passenger;
import com.itsoul.lab.domains.PassengerList;

@Path("/passenger")
public class PassengerService {

	@GET @Path("/{location}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPassengerAt(@PathParam("location") Integer offset, @PathParam("size") Integer limit) {
		String error = "";
		try {
			Connection conn = JDBConnectionPool.connection("testDB");
			SQLExecutor exe = new SQLExecutor(conn);
			
			SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT)
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
			
			SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT)
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
			ORMService<Passenger> service = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);
			
			JPQLSelectQuery query = new JPQLQuery.Builder(QueryType.SELECT)
											.columns()
											.from(service.getEntity())
											.orderBy("id")
											.build();
			Query iQuery = service.getEntityManager().createQuery(query.toString());
			iQuery.setFirstResult(offset);
			iQuery.setMaxResults(limit);
			@SuppressWarnings("unchecked")
			List<Passenger> passengers = iQuery.getResultList();
			
			PassengerList list = new PassengerList();
			for (Passenger item : passengers) {
				list.add(item);
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
			ORMService<Passenger> allPass = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);
			Passenger passenger = allPass.findBy(new Property("id", id, DataType.INT));
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
			ORMService<Passenger> service = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);
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
			List<Passenger> items = (List<Passenger>) service.findMatches(and);
			PassengerList list = new PassengerList();
			for (Passenger item : items) {
				list.add(item);
			}
			return Response.status(200).entity(list).build();
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
	@POST @Path("JPA/create")
	@Produces(MediaType.APPLICATION_JSON) 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNew(Passenger passenger) {
		String error = "";
		try {
			ORMService<Passenger> service = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);
			service.insert(passenger);
			return Response.status(200).entity("Created").build();
		}catch(Exception e) {
			error = e.getMessage();
		}
		return Response.status(500).entity(error).build();
	}
	
}
