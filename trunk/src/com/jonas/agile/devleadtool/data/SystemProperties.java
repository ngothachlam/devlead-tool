package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SystemProperties {
	private static final String PROPERTYFILENAME = "fields.properties";

	private static Properties properties;

	public static String getProperty(String propertyName, String defaultValue) {
		return getProperties().getProperty(propertyName, defaultValue);
	}

	private static Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(new File(PROPERTYFILENAME)));
			} catch (IOException e) {
				System.err.println(e);
			}
		}

		return properties;
	}

	public static void setProperty(String propertyName, String propertyValue) {
		getProperties().setProperty(propertyName, propertyValue);
	}

	public static void close() {
		try {
			getProperties().store(new FileOutputStream(new File(PROPERTYFILENAME)), "Field Properties");
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
