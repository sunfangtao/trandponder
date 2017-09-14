package com.aioute.log4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class log4jListener implements ServletContextListener {

    public static final String log4jdirkey = "log4jdir";

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.getProperties().remove(log4jdirkey);
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String log4jdir = servletContextEvent.getServletContext().getRealPath("/");
        System.setProperty(log4jdirkey, log4jdir);
    }
} 