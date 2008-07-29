package com.jonas.logging;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {
	
	static {
		BasicConfigurator.configure();
		
		PropertyConfigurator.configure("properties/log4j.properties");

	}

	public static Logger getLogger(Class className) {
		return Logger.getLogger(className);
	}

}
