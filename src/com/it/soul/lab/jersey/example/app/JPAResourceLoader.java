package com.it.soul.lab.jersey.example.app;

import javax.persistence.EntityManager;

import com.it.soul.lab.service.ORMController;

public class JPAResourceLoader {
	
	private static JPAResourceLoader shared;
	private ORMController controller;

	private JPAResourceLoader() {
		super();
		controller = new ORMController("testDB");
	}
	
	public static synchronized void configure() {
		if(shared == null){
			shared = new JPAResourceLoader();
        }
	}
	
	public static synchronized EntityManager entityManager() {
		JPAResourceLoader.configure();
		return shared.controller.getEntityManager();
	}

}
