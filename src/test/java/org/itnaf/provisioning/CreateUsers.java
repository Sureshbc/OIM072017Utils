package org.itnaf.provisioning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.security.auth.login.LoginException;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.identity.exception.UserAlreadyExistsException;
import oracle.iam.identity.exception.UserCreateException;
import oracle.iam.identity.exception.ValidationFailedException;
// User related API's
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.request.api.RequestService;

public class CreateUsers {

	private static String DIRECTORY = "/Users/mfanti/Programming Development/eclipseworkspace/oimclient/conf/authwl.conf";
	private static final String APPSERVER_TYPE = "wls";
	private static final String CLASS_NAME = null;
	private static String TARGET = null;
	private static OIMClient oimClient;

	/*
	 * Initialize the context and login with client supplied environment
	 */
	public void init() throws Exception {
		TARGET = "OIAM11G";		
		TARGET = "AWS";
		TARGET = "OIAM11GMAC";		
		
		// Set the one to test as last
		TARGET = "OIAM11G";		
		
        oimClient = Init.init(TARGET);
	}

	
	public OIMClient getOIMClient() {
		return oimClient;
	}
	
	public tcUserOperationsIntf getUserOperationsIntf() {
		return oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
	}
	
	public RequestService getRequestService() throws Exception {
		return oimClient.getService(RequestService.class);
	}

	/**
	 * Generates random string for inputs
	 */
	public static String getRandomString(int n) {
		char[] pw = new char[n];
		int c = 'A';
		int r1 = 0;
		for (int i = 0; i < n; i++) {
			r1 = (int) (Math.random() * 3);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 10);
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 26);
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 26);
				break;
			}
			pw[i] = (char) c;
		}
		return new String(pw);
	}

	public String [] createUsers(int howMany) {
		
		String [] newUsers = new String [howMany];
		UserManager userService = oimClient.getService(UserManager.class);
		Set<String> retAttrs = new HashSet<String>();

		try {		
			for (int i = 0; i < howMany; i++) {
				String id = getRandomString(7);
				tcUserOperationsIntf userOperationsIntf = 
						oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
				String managerKey = "" + getUserKey(userOperationsIntf, Init.MANAGER);
				
				System.out.println("Manager key " + managerKey);
				User user = new User(null); // Pass null while creating
				user.setManagerKey(managerKey);
				String loginId = "User." + i;
				user.setAttribute("User Login", loginId);
				user.setAttribute("First Name", loginId);
				user.setAttribute("Last Name", loginId);
				user.setAttribute("Email", loginId + "@mail.com");
				user.setAttribute("act_key", 21L); // Replace 1L with the
													// organization key long value
				user.setAttribute("Role", "Full-Time");
				user.setAttribute("Xellerate Type", "End-User");
				userService.create(user);
				
				user.setManagerKey(managerKey);
				
				HashMap hm = user.getAttributes();
				newUsers[i] = user.getLogin();
				System.out.println(hm);		
			}

		} catch (AccessDeniedException ade) {
			// handle exception
		} catch (ValidationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newUsers;
	}

	public String createUser(String login) {
		
		UserManager userService = oimClient.getService(UserManager.class);
		Set<String> retAttrs = new HashSet<String>();

		try {		
				tcUserOperationsIntf userOperationsIntf = 
						oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
				String managerKey = "" + getUserKey(userOperationsIntf, Init.MANAGER);
				
				System.out.println("Manager key " + managerKey);
				User user = new User(null); // Pass null while creating
				user.setManagerKey(managerKey);
				String loginId = login;
				user.setAttribute("User Login", loginId);
				user.setAttribute("First Name", loginId);
				user.setAttribute("Last Name", loginId);
				user.setAttribute("Email", loginId + "@mail.com");
				user.setAttribute("act_key", 21L); // Replace 1L with the
													// organization key long value
				user.setAttribute("Role", "Full-Time");
				user.setAttribute("Xellerate Type", "End-User");
				userService.create(user);
				
				user.setManagerKey(managerKey);
				
				HashMap hm = user.getAttributes();
				System.out.println(hm);		
		} catch (AccessDeniedException ade) {
			return null;
		} catch (ValidationFailedException e) {
			return null;
		} catch (UserAlreadyExistsException e) {
			return null;
		} catch (UserCreateException e) {
			return null;
		}
		return login;
	}

	public static long getUserKey(tcUserOperationsIntf usrIntf, String usrLogin) {
		long usrKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Users.User ID", usrLogin);
			tcResultSet rs = usrIntf.findUsers(hm);
			rs.goToRow(0);
			usrKey = rs.getLongValue("Users.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usrKey;
	}


	public static String run(String login) {
		try {
			CreateUsers oimSample = new CreateUsers();

			// initialize resources
			oimSample.init();

			return oimSample.createUser(login);
			
		} catch (Exception e) {
			return null;
		}
	
	}
	
	public static void main(String args[]) {

		List moList = null;

		try {
			CreateUsers oimSample = new CreateUsers();

			// initialize resources
			oimSample.init();

			int howMany = 6;
			String [] usrLoginArray = null;
			usrLoginArray = oimSample.createUsers(howMany);
			
			for (int i = 0; i < howMany; i++) {
				System.out.println("Created user " + usrLoginArray[i]);
			}

			oimClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
