package org.itnaf.metadata.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itnaf.metadata.exceptions.OIMNotInitializedException;
import org.itnaf.utils.Client;
import org.itnaf.utils.ProcessFormClient;
import org.itnaf.utils.RequestClient;
import org.itnaf.utils.UserClient;

import com.thortech.xl.dataaccess.tcDataProvider;

import Thor.API.Exceptions.tcAPIException;

import java.util.logging.Logger;

import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.request.exception.NoRequestPermissionException;
import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.vo.RequestConstants;

/**
 * 
 * Utility class to parse requests for User, Request, Process Form or Child Form data
 * 
 * Returns a HashMap where the keys are each of the values searched, and the value is an 
 * ArrayList of entries that match the field
 *
 */
public class FieldOrigin {
	final static Logger LOGGER = Logger.getLogger(FieldOrigin.class.getName());
	public static final String USER_PROFILE = "UP";
	public static final String REQUEST_DATA = "RD";
	public static final String PROCESS_FORM = "PF";
	public static final String PROCESS_CHILD_FORM = "PCF";
	public static final String OTHER_PROCESS_FORM = "OPF";
	public static final String OTHER_PROCESS_CHILD_FORM = "OPCF";
	public static final String APPLICATION_STATUS = "AS";

	private long processInstanceKey = -1L;
	private long userKey = -1L;
	private Client client = null;
	private HashMap<String, String> requestSearchParameters = null;
	private HashMap<String, String> userSearchParameters = null;

	/**
	 * 
	 * @param dataProvider - tcDataProvider used to obtain OIM interfaces
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public FieldOrigin(tcDataProvider dataProvider) throws tcAPIException, OIMNotInitializedException {
		client = new Client(dataProvider);
	}

	/**
	 * 
	 * @param oimClient  - OIMClient used to obtain OIM interfaces
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public FieldOrigin(OIMClient oimClient) throws tcAPIException, OIMNotInitializedException {
		client = new Client(oimClient);
	}
	
	public void setProcessInstanceKey(String processInstanceKeyString) {
		this.processInstanceKey = Long.parseLong(processInstanceKeyString);
		
	}
	
	
	public void setUserKey(long userKey) {
		this.userKey = userKey;
	}

	/**
	 * 
	 * @param oimClient
	 * @param userLogin
	 * @param requestKey
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public FieldOrigin(OIMClient oimClient, String userLogin, String requestKey) 
			throws tcAPIException, OIMNotInitializedException {
		client = new Client(oimClient);
		userSearchParameters = new HashMap<String, String>();
		userSearchParameters.put("User Login", userLogin);
		requestSearchParameters = new HashMap<String, String>();
		requestSearchParameters.put(RequestConstants.REQUEST_KEY, requestKey);
	}

	/**
	 * 
	 * @param oimClient
	 * @param userSearchParameters
	 * @param requestSearchParameters
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public FieldOrigin(OIMClient oimClient, HashMap<String, String> userSearchParameters,
			HashMap<String, String> requestSearchParameters) throws tcAPIException, OIMNotInitializedException {
		client = new Client(oimClient);
		this.requestSearchParameters = requestSearchParameters;
		this.userSearchParameters = userSearchParameters;
	}

	/**
	 * 
	 * @param oimClient
	 * @param userSearchParameters
	 * @throws OIMNotInitializedException 
	 * @throws tcAPIException 
	 */
	public FieldOrigin(OIMClient oimClient, HashMap<String, String> userSearchParameters) 
			throws tcAPIException, OIMNotInitializedException {
		client = new Client(oimClient);
		this.userSearchParameters = userSearchParameters;
	}

	// HashMap ("UF.FF1 - ArrayLKist", "UP.Last Name - ArrayList"
	
	public FieldOriginResult getFieldOriginValuesHashMap(String key) throws UserSearchException {
		if (key == null) {
			return null;
		}
		key = key.trim();
		// First separate by the commas
		ArrayList <String> allItems = ParseUtils.parseDelimitedStrings(",", key);
		
		// Then organize together (all UP, all FF, etc...)
		ArrayList <ArrayList <String>> userProfileKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> requestDataKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> processFormKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> processChildFormKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> otherProcessFormKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> otherProcessChildFormKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> applicationStatusKeyList = new ArrayList <ArrayList <String>> ();
		ArrayList <ArrayList <String>> otherKeyList = new ArrayList <ArrayList <String>> ();
		
		for (int howMany = 0; howMany < allItems.size(); howMany++) {
			key = allItems.get(howMany);
			if (key.startsWith(USER_PROFILE + ".")) {
				userProfileKeyList.add(ParseUtils.parseDotDelimited(key.substring(USER_PROFILE.length() + 1)));
			} else if (key.startsWith(FieldOrigin.REQUEST_DATA + ".")) {
				requestDataKeyList.add(ParseUtils.parseDotDelimited(key.substring(REQUEST_DATA.length() + 1)));
			} else if (key.startsWith(FieldOrigin.PROCESS_FORM + ".")) {
				processFormKeyList.add(ParseUtils.parseDotDelimited(key.substring(PROCESS_FORM.length() + 1)));
			} else if (key.startsWith(FieldOrigin.PROCESS_CHILD_FORM + ".")) {
				processChildFormKeyList.add(ParseUtils.parseDotDelimited(key.substring(PROCESS_CHILD_FORM.length() + 1)));
			} else if (key.startsWith(FieldOrigin.OTHER_PROCESS_FORM + ".")) {
				otherProcessFormKeyList.add(ParseUtils.parseDotDelimited(key.substring(APPLICATION_STATUS.length() + 1)));
			} else if (key.startsWith(FieldOrigin.OTHER_PROCESS_CHILD_FORM + ".")) {
				otherProcessChildFormKeyList.add(ParseUtils.parseDotDelimited(key.substring(APPLICATION_STATUS.length() + 1)));
			} else if (key.startsWith(FieldOrigin.APPLICATION_STATUS + ".")) {
				applicationStatusKeyList.add(ParseUtils.parseDotDelimited(key.substring(APPLICATION_STATUS.length() + 1)));
			} else {
				ArrayList <String> otherKey = new ArrayList <String>();
				otherKey.add(key);
				otherKeyList.add(otherKey);
			}
		}
		HashMap <String, String> fieldOriginResultHashMap = new HashMap <String, String>();
		FieldOriginResult fieldOriginResult = 
				new FieldOriginResult(fieldOriginResultHashMap, Long.toString(processInstanceKey), "", Long.toString(userKey));
				
		// Now process each type one by one
		if (!userProfileKeyList.isEmpty()) {
			UserClient userClient;
			try {
				userClient = new UserClient(client);
				fieldOriginResult = userClient.getUserAttributes(userProfileKeyList, fieldOriginResult, userKey);
			} catch (tcAPIException e) {
			} catch (OIMNotInitializedException e) {
			}
		}
		if (!requestDataKeyList.isEmpty()) {
			RequestClient requestClient;
			try {
				requestClient = new RequestClient(client);
//				return requestClient.getSpecificRequestAttributes(requestSearchParameters, keys);
			} catch (Exception e) {
			}
		}
		if (!processFormKeyList.isEmpty()) {
			ProcessFormClient processFormClient = new ProcessFormClient(client);
			processFormClient.getProcessFormFields(processFormKeyList, fieldOriginResult, processInstanceKey);
		}
		
		if (!processChildFormKeyList.isEmpty()) {
			ProcessFormClient processFormClient = new ProcessFormClient(client);
			processFormClient.getUDFChildAttributes(processChildFormKeyList, fieldOriginResult, processInstanceKey);
		}
		
		if (!otherProcessFormKeyList.isEmpty()) {
		}
		
		if (!otherProcessChildFormKeyList.isEmpty()) {
		}
		
		if (!applicationStatusKeyList.isEmpty()) {
		}
		
		if (!otherKeyList.isEmpty()) {
		}
		return fieldOriginResult;
	}
	
}
