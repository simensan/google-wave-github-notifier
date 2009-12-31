package com.fauxpas;

import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ServletContextListener extends GuiceServletContextListener {
	@Override 
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("/_wave/robot/jsonrpc").with(FauxpasRobotServlet.class);
				serve("/_wave/robot/profile").with(FauxpasProfileServlet.class);
				serve("/commit/*").with(FauxpasCommitServlet.class);
			}
	    });
	}
}
