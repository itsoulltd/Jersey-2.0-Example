package com.it.soul.lab.jersey.example.app;

import org.glassfish.jersey.server.ResourceConfig;

public class WebApplication extends ResourceConfig{

	public WebApplication() {
		packages("com.it.soul.lab.api.services");
		register(SecuredAuthorizationFilter.class);
		JPAResourceLoader.configure();
	}
}
