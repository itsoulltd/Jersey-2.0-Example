package com.it.soul.lab.api.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.it.soul.lab.connect.JDBConnectionPool;
import com.it.soul.lab.jersey.example.app.SecuredAuthorization;
import com.it.soul.lab.service.ORMController;
import com.it.soul.lab.service.ORMService;
import com.it.soul.lab.service.jpa.models.JPAPassenger;
import com.it.soul.lab.service.models.Passenger;
import com.it.soul.lab.service.models.PassengerList;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.query.SQLQuery;
import com.it.soul.lab.sql.query.SQLQuery.QueryType;
import com.it.soul.lab.sql.query.SQLSelectQuery;
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
			ORMController controller = new ORMController("testDB");
			ORMService<JPAPassenger> allPass = new ORMService<>(controller.getEntityManager(), JPAPassenger.class);
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
	
}
