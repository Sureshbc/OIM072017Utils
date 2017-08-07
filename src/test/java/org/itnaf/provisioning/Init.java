package org.itnaf.provisioning;

import java.util.Hashtable;

import oracle.iam.platform.OIMClient;

public class Init {
	private static String OIM_URL = "t3://host01:14000"; 
	private static String AUTH_CONF = "C:/temp/designconsole/config/authwl.conf"; 
	private static final String APPSERVER_TYPE = "wls";
	protected static final String OIM_USERNAME = "xelsysadm";
	public static String APPLICATION_INSTANCE_NAME = "OUDUser"; // Attribute Name for Name
	public static String IT_RESOURCE_NAME = "SAYHELLO"; // Attribute Name for Name
	public static String RESOURCE_ATTR_NAME_STRING = "LDAP User"; // Attribute Name for Name
	public static String CONTAINER_DN = "Local OUD~People"; // Attribute Name for Name
	public static String LOGIN = "login"; // Attribute Name for Name
	public static String MANAGER = "TESTUSER1";
	protected static String OIM_PASSWORD = "Welcome1"; 
	public static OIMClient oimClient = null;
	
	public static OIMClient init(String target) throws Exception {
		
		if ("OIAM11G".equalsIgnoreCase(target)) {
			OIM_URL = "t3://host01:14000"; // OIM 11g deployment
			OIM_PASSWORD = "Welcome1";
			AUTH_CONF = "C:/temp/designconsole/config/authwl.conf";
			APPLICATION_INSTANCE_NAME = "OUDUser";
			MANAGER = "TESTUSER1";
		} else if ("OIAM11GMAC".equalsIgnoreCase(target)) {
			OIM_URL = "t3://oiam11gmac:14000"; // OIM 11g deployment
			OIM_PASSWORD = "Demo12345";
			AUTH_CONF = "D:/temp/DesignConsole/config/authwl.conf";
			APPLICATION_INSTANCE_NAME = "LDAPLocal";
			CONTAINER_DN = "LDAP Local~People";
			MANAGER = "TESTUSER1";
		} else if ("AWS".equalsIgnoreCase(target)) {
			OIM_URL = "t3://snp2.aurionprosena.org:14000"; // OIM 11g deployment
			OIM_PASSWORD = "Oracle123";
			AUTH_CONF = "D:/temp/DesignConsole/config/authwl.conf";
			APPLICATION_INSTANCE_NAME = "LDAPUserAWS";
			MANAGER = "MFANTI";
		}
		
		System.out.println("URL " + OIM_URL);
		System.out.println("OIM_PASSWORD " + OIM_PASSWORD);
		System.out.println("AUTH_CONF " + AUTH_CONF);
		Hashtable<String, String> env = new Hashtable<String, String>();

		System.setProperty("java.security.auth.login.config", AUTH_CONF);
		System.setProperty("APPSERVER_TYPE", APPSERVER_TYPE);

		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, OIM_URL);

		 oimClient = new OIMClient(env);

		 oimClient.login(OIM_USERNAME, OIM_PASSWORD.toCharArray());

		return oimClient;
	}
	
	public static OIMClient init(String url, String password, String conf) throws Exception {
		
		OIM_URL = url;
		OIM_PASSWORD = password;
		AUTH_CONF = conf;
		
		System.out.println("URL " + OIM_URL);
		System.out.println("OIM_PASSWORD " + OIM_PASSWORD);
		System.out.println("AUTH_CONF " + AUTH_CONF);
		Hashtable<String, String> env = new Hashtable<String, String>();

		System.setProperty("java.security.auth.login.config", AUTH_CONF);
		System.setProperty("APPSERVER_TYPE", APPSERVER_TYPE);

		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, OIM_URL);

		 oimClient = new OIMClient(env);

		 oimClient.login(OIM_USERNAME, OIM_PASSWORD.toCharArray());

		return oimClient;
	}

}
