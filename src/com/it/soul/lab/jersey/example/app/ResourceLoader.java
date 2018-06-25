package com.it.soul.lab.jersey.example.app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.it.soul.lab.connect.JDBConnectionPool;


public class ResourceLoader implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		JDBConnectionPool.configureConnectionPool("java:comp/env/jdbc/testDB");
		System.out.println("jdbc/testDB is uploaded.");
	}

}
