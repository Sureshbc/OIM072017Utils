package org.itnaf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.itnaf.metadata.exceptions.OIMNotInitializedException;
import org.itnaf.metadata.utils.FieldOriginResult;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

public class UserClient {
	final static Logger LOGGER = Logger.getLogger(UserClient.class.getName());
	private tcUserOperationsIntf userOperationsIntf;

	private Client client = null;

	public UserClient(Client client) throws tcAPIException, OIMNotInitializedException {
		this.client = client;
		userOperationsIntf = client.getUserOperationsIntf();
	}

	protected UserManager getUserManager() throws Exception {
		UserManager umgr = null;

		umgr = client.getOIMClient().getService(UserManager.class);

		LOGGER.info("UserManager ready");

		return umgr;
	}
	
	/**
	 * Add new values found to the list of existing values
	 * @param attributeNames
	 * @param fieldOriginResult
	 * @param userKey
	 * @return
	 */
	public FieldOriginResult getUserAttributes(ArrayList <ArrayList <String>> attributeNames, 
			FieldOriginResult fieldOriginResult, long userKey) {
		int numberToFetch = attributeNames.size();
		
		HashMap <String, String> usrMap = new HashMap <String, String> ();
		usrMap.put("Users.Key", Long.toString(userKey));

		tcResultSet usrRS;
		try {
			usrRS = client.getUserOperationsIntf().findUsers(usrMap);
			// Can find only one user
			if (usrRS.getRowCount() == 0) {
				return fieldOriginResult;
			}
			usrRS.goToRow(0);

			String[] columnNames = usrRS.getColumnNames();

			HashMap<String, String> existingValues = fieldOriginResult.getValues();
			for (int fetching = 0; fetching < numberToFetch; fetching++) {
				for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
					String originalAttributeName = attributeNames.get(fetching).get(0);
					String attributeName = "Users." + attributeNames.get(fetching).get(0);
					if (attributeName.equalsIgnoreCase(columnNames[columnIndex]) ||
							originalAttributeName.equalsIgnoreCase(columnNames[columnIndex])) {
						String value = usrRS.getStringValueFromColumn(columnIndex);
						existingValues.put("UP." + originalAttributeName, value);
						break;
					}
				}
			}
			fieldOriginResult.setValues(existingValues);
			return fieldOriginResult;
		} catch (tcAPIException e) {
			return fieldOriginResult;
		} catch (tcColumnNotFoundException e) {
			return fieldOriginResult;
		}
	}
	
	/**
	 * Get the user key based on the user login
	 * 
	 * @param usrLogin
	 * @return
	 */
	public String getUserKey(String usrLogin) {
		return Long.toString(OIMUtils.getUserKey(client.getUserOperationsIntf(), usrLogin), 0);
	}
	
	/*
	 * 


FA Territory, Employee Number, 
Users.Middle Name, 
Manually Locked, 
Users.Disable User,
 USR_HAS_HIGH_RISK_OPEN_SOD,
  Users.Display Name, 
  Users.Lock User, 
  Users.Ldap Organization, 
  Currency, 
  USR_HAS_HIGH_RISK_PROV_METH, 
  Time Format, 
  USR_CREATED, 
  Users.Deprovisioning Date, 
  USR_FULL_NAME, 
  Accessibility Mode, 
  Users.Country, 
  Users.Password Expiration Date, 
  USR_ROLE_SUMMARY_RISK, 
  Users.Password Cannot Change, 
  Users.Email, Users.System Level, 
  Automatically Delete On, 
  Locked On, 
  Users.Login Attempts Counter, 
  Users.Last Name, 
  Users.First Name, 
  Locality Name, 
  usr_policy_update, 
  Street, 
  Embedded Help, 
    Department Number, 
    Users.Created By, 
    USR_ACCOUNT_SUMMARY_RISK, 
    Users.Password Warned, 
    MANAGER LOGIN, 
    USERS.MANAGER LOGIN, 
    Telephone Number, 
    MANAGER FIRST NAME, 
    USERS.MANAGER FIRST NAME, 
    USR_JOB_CODE, 
    Users.Updated By, 
    Home Phone, 
    LDAP Organization Unit, 
    usr_pwd_min_age_date, 
    Users.User ID, Title, 
    Users.Role, 
    Password Generated, 
    FA Language, 
    Users.Provisioning Date, 
    Users.Password Warning Date, 
    ORGANIZATION NAME, usr_locale, 
    Users.Update Date, 
    USR_RISK_UPDATE_DATE, 
    USR_HAS_HIGH_RISK_LAST_CERT, 
    Date Format, usr_timezone, 
    Mobile, 
    Users.Password Reset Attempts Counter, 
    USR_UDF_NSUNIQUEID, USR_HAS_HIGH_RISK_RESOURCE, 
    Users.End Date, 
    Users.Deprovisioned Date, 
    Pager, Color Contrast, PO Box, 
    Users.Creation Date, Users.Ldap Guid, 
    Users.Xellerate Type, 
    Users.Change Password At Next Logon, 
    USR_UDF_ORCLGUID, Users.Provisioned Date, 
    Common Name, USR_SUMMARY_RISK, 
    Users.Start Date, 
    Users.Manager Key, 
    Number Format, 
    Users.Password Expired, 
    Hire Date, USR_HAS_HIGH_RISK_ENTITLEMENT, 
    Home Postal
     Address, USR_NAME_PREFERRED_LANG, Font Size, 
     MANAGER LAST NAME, USERS.MANAGER LAST NAME, Description, 
     Fax, Postal Code, 
     USR_ENTITLEMENT_SUMMARY_RISK, 
     Organizations.Key, 
     Organizations.Organization Name, 
     Users.Key, USR_CN_GENERATED, 
     Users.Status, 
     Generation Qualifier, 
     Postal Address, State, 
     MANAGER DISPLAY NAME, 
     USERS.MANAGER DISPLAY NAME, 
     USR_HAS_HIGH_RISK_ROLE, 
     Initials, 
     Users.Password Never Expires, 
     Users.Password Must Change, 
     LDAP DN, USR_OFFICE_NAME, 
     MEMBERTYPE]
*/

}
