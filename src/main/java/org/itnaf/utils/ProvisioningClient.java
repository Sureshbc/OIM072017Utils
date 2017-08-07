package org.itnaf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.metadata.utils.SearchResults;
import org.itnaf.metadata.exceptions.OIMNotInitializedException;

import Thor.API.Exceptions.tcAPIException;
import oracle.iam.catalog.api.CatalogService;
import oracle.iam.catalog.vo.Catalog;
import oracle.iam.catalog.vo.CatalogSearchCriteria;
import oracle.iam.catalog.vo.CatalogSearchResult;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.request.exception.NoRequestPermissionException;
import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.Request;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;
import oracle.iam.request.vo.RequestConstants;

/**
 * 
 * @author mfanti
 *
 */
public class ProvisioningClient {
	final static Logger LOGGER = Logger.getLogger(ProvisioningClient.class.getName());
	protected ProvisioningService service = null;

	private Client client = null;
	ProvisioningService provisioningService = null;
	
	public ProvisioningClient(Client client) 
			throws tcAPIException, OIMNotInitializedException {
		this.client = client;
		provisioningService = getProvisioningService();
	}
	
	public ProvisioningService getProvisioningService() {
		return client.getOIMClient().getService(ProvisioningService.class);
	}

	public void getSpecificRequestAttributes(String userId) throws UserNotFoundException, GenericProvisioningException {
		List accounts = provisioningService.getAccountsProvisionedToUser(userId);
		if (accounts != null) {
		System.out.println(accounts);
		} else {
			System.out.println("null");
		}
	}

}
