package org.itnaf.utils;


import java.util.Hashtable;
import java.util.logging.Logger;

import org.itnaf.metadata.exceptions.OIMNotInitializedException;
import com.thortech.xl.dataaccess.tcDataProvider;

import Thor.API.tcUtilityFactory;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.platform.OIMClient;

/**
 * 
 * @author Marco Fanti
 *
 */
@SuppressWarnings("unused")
public class Client {
	final static Logger LOGGER = Logger.getLogger(Client.class.getName());
	private tcUtilityFactory apiFactory;
	private tcFormInstanceOperationsIntf formInstanceOperationsIntf = null;
	private tcLookupOperationsIntf lookupOperationsIntf = null;
	private tcFormDefinitionOperationsIntf formDefinitionOperationsIntf = null;
	private tcUserOperationsIntf userOperationsIntf = null;
	private tcITResourceInstanceOperationsIntf itResourceInstanceOperationsIntf = null;
	private boolean initialized = false;
	
	private static String AUTH_CONF = "C:/temp/designconsole/config/authwl.conf"; 
	private static final String APPSERVER_TYPE = "wls";
	protected OIMClient oimClient = null;
	private tcDataProvider dataProvider = null;

	/**
	 * 
	 * @param oimClient
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public Client(OIMClient oimClient) throws tcAPIException, OIMNotInitializedException {
		super();
		this.oimClient = oimClient;
		init();
	}

	/**
	 * 
	 * @param dataProvider
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public Client(tcDataProvider dataProvider) throws tcAPIException, OIMNotInitializedException {
		super();
		this.dataProvider = dataProvider;
		init();
	}

	public void init() throws tcAPIException, OIMNotInitializedException {
		if (dataProvider != null) {
			formInstanceOperationsIntf = (tcFormInstanceOperationsIntf) tcUtilityFactory.getUtility(dataProvider,
					"Thor.API.Operations.tcFormInstanceOperationsIntf");
			lookupOperationsIntf = (tcLookupOperationsIntf) tcUtilityFactory.getUtility(dataProvider,
					"Thor.API.Operations.tcLookupOperationsIntf");
			formDefinitionOperationsIntf = (tcFormDefinitionOperationsIntf) tcUtilityFactory.getUtility(dataProvider,
					"Thor.API.Operations.tcFormDefinitionOperationsIntf");
			userOperationsIntf = (tcUserOperationsIntf) tcUtilityFactory.getUtility(dataProvider,
					"Thor.API.Operations.tcUserOperationsIntf");
			itResourceInstanceOperationsIntf = (tcITResourceInstanceOperationsIntf) tcUtilityFactory.getUtility(dataProvider,
					"Thor.API.Operations.tcITResourceInstanceOperationsIntf");
		} else if (oimClient != null) {
			formInstanceOperationsIntf = oimClient.getService(Thor.API.Operations.tcFormInstanceOperationsIntf.class);
			lookupOperationsIntf = oimClient.getService(Thor.API.Operations.tcLookupOperationsIntf.class);
			formDefinitionOperationsIntf = oimClient.getService(Thor.API.Operations.tcFormDefinitionOperationsIntf.class);
			userOperationsIntf = oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
			itResourceInstanceOperationsIntf = oimClient.getService(Thor.API.Operations.tcITResourceInstanceOperationsIntf.class);
		} else {
			throw new OIMNotInitializedException("OIM is not initialized");
		}
	}
	
	public OIMClient getOIMClient() {
		return oimClient;
	}
	
	/**
	 * Utility class to get OIMClient during local tests
	 * 
	 * @param userName
	 * @param password
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public static OIMClient getOIMClient(String userName, String password, String URL) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();

		System.setProperty("java.security.auth.login.config", AUTH_CONF);
		System.setProperty("APPSERVER_TYPE", APPSERVER_TYPE);

		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, URL);

		OIMClient oimClient = new OIMClient(env);
		LOGGER.info("Logging in with userName: " + userName + " and URL: " + URL);
		oimClient.login(userName, password.toCharArray());
		LOGGER.info("Logged in with userName: " + userName + " and URL: " + URL);
		return oimClient;
	}
	
	/**
	 * Utility class to get OIMClient during local tests
	 * 
	 * @param userName
	 * @param password
	 * @param URL
	 * @param authConf
	 * @return
	 * @throws Exception
	 */
	public static OIMClient getOIMClient(String userName, String password, String URL, 
			String authConf) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();

		System.setProperty("java.security.auth.login.config", authConf);
		System.setProperty("APPSERVER_TYPE", APPSERVER_TYPE);

		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, URL);

		OIMClient oimClient = new OIMClient(env);
		LOGGER.info("Logging in with userName: " + userName);
		oimClient.login(userName, password.toCharArray());
		LOGGER.info("Logged in with userName: " + userName);
		return oimClient;
	}

	public tcFormInstanceOperationsIntf getFormInstanceOperationsIntf() {
		return formInstanceOperationsIntf;
	}

	public void setFormInstanceOperationsIntf(tcFormInstanceOperationsIntf formInstanceOperationsIntf) {
		this.formInstanceOperationsIntf = formInstanceOperationsIntf;
	}

	public tcLookupOperationsIntf getLookupOperationsIntf() {
		return lookupOperationsIntf;
	}

	public void setLookupOperationsIntf(tcLookupOperationsIntf lookupOperationsIntf) {
		this.lookupOperationsIntf = lookupOperationsIntf;
	}

	public tcFormDefinitionOperationsIntf getFormDefinitionOperationsIntf() {
		return formDefinitionOperationsIntf;
	}

	public void setFormDefinitionOperationsIntf(tcFormDefinitionOperationsIntf formDefinitionOperationsIntf) {
		this.formDefinitionOperationsIntf = formDefinitionOperationsIntf;
	}

	public tcUserOperationsIntf getUserOperationsIntf() {
		return userOperationsIntf;
	}

	public void setUserOperationsIntf(tcUserOperationsIntf userOperationsIntf) {
		this.userOperationsIntf = userOperationsIntf;
	}

	public tcITResourceInstanceOperationsIntf getItResourceInstanceOperationsIntf() {
		return itResourceInstanceOperationsIntf;
	}

	public void setItResourceInstanceOperationsIntf(tcITResourceInstanceOperationsIntf itResourceInstanceOperationsIntf) {
		this.itResourceInstanceOperationsIntf = itResourceInstanceOperationsIntf;
	}
}

