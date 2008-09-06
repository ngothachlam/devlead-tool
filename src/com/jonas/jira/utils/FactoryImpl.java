package com.jonas.jira.utils;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraSoapClient;

public class FactoryImpl implements Factory {

	private Logger log = MyLogger.getLogger(FactoryImpl.class);

	@Override
	public JiraSoapClient getJiraSoapClient(String address) {
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = getLocator(address);
		JiraSoapService jirasoapserviceV2 = null;
		try {
			jirasoapserviceV2 = jiraSoapServiceServiceLocator.getJirasoapserviceV2();
			return new JiraSoapClient(jirasoapserviceV2);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JiraSoapServiceServiceLocator getLocator(String address) {
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
		jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(address);
		log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
		return jiraSoapServiceServiceLocator;
	}
}
