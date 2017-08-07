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
import oracle.iam.request.api.RequestService;
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
public class RequestClient {
	final static Logger LOGGER = Logger.getLogger(RequestClient.class.getName());
	protected RequestService service = null;

	private Client client = null;
	
	public RequestClient(Client client) 
			throws tcAPIException, OIMNotInitializedException {
		this.client = client;
	}
	
	public RequestService getRequestService() {
		return client.getOIMClient().getService(RequestService.class);
	}

	public ArrayList<FieldOriginResult> getSpecificRequestAttributes(
			HashMap<String, String> requestSearchParameters, ArrayList<String> keys)
					throws AccessDeniedException, RequestServiceException, NoRequestPermissionException {
		ArrayList<FieldOriginResult> requestFieldOriginResults = new ArrayList<FieldOriginResult>();
		ArrayList<SearchResults> requestAttributes = getRequestAttributes(requestSearchParameters, keys.get(0),
				keys.get(1), 0L, null, null);
		for (SearchResults searchResult : requestAttributes) {
//			FieldOriginResult requestFieldOriginResult = new FieldOriginResult(keys.get(0), keys.get(1),
//					searchResult.attributes, null, searchResult.key, null);
//			requestFieldOriginResults.add(requestFieldOriginResult);
		}
		return requestFieldOriginResults;
	}

	/**
	 * 
	 * @param userSearchParameters
	 * @param requestSearchParameters
	 * @param keys
	 * @return
	 * @throws AccessDeniedException
	 * @throws RequestServiceException
	 * @throws NoRequestPermissionException
	 */
	// 0 = dbuser1 1 - ud_.... 2 - CHILD
	public ArrayList<FieldOriginResult> getSpecificProcessFormAttributes(
			HashMap<String, String> userSearchParameters, HashMap<String, String> requestSearchParameters,
			ArrayList<String> keys)
					throws AccessDeniedException, RequestServiceException, NoRequestPermissionException {
		String userLogin = userSearchParameters.get("User Login");
		UserClient userClient = null;
		
		try {
			userClient = new UserClient(client);
		} catch (tcAPIException e1) {
			e1.printStackTrace();
		} catch (OIMNotInitializedException e1) {
			e1.printStackTrace();
		}
			
		long userKey = 0L;
		try {
			userKey = OIMUtils.getUserKey(client.getUserOperationsIntf(), userLogin);
		} catch (Exception e) {
		}
		if (userKey == 0L) {
			return null;
		}
		ArrayList<FieldOriginResult> requestFieldOriginResults = new ArrayList<FieldOriginResult>();
		ArrayList<SearchResults> requestAttributes = getRequestAttributes(requestSearchParameters, keys.get(2),
				keys.get(1), userKey, keys.get(0), null);
		for (SearchResults searchResult : requestAttributes) {
//			FieldOriginResult requestFieldOriginResult = new FieldOriginResult(keys.get(2), keys.get(0),
//					searchResult.attributes, null, searchResult.key, null);
//			requestFieldOriginResults.add(requestFieldOriginResult);
		}
		return requestFieldOriginResults;
	}

	/**
	 * 
	 * @param userSearchParameters
	 * @param requestSearchParameters
	 * @param keys
	 * @return
	 * @throws AccessDeniedException
	 * @throws RequestServiceException
	 * @throws NoRequestPermissionException
	 */
	public ArrayList<FieldOriginResult> getSpecificChildProcessFormAttributes(
			HashMap<String, String> userSearchParameters, HashMap<String, String> requestSearchParameters,
			ArrayList<String> keys)
					throws AccessDeniedException, RequestServiceException, NoRequestPermissionException {
		String userLogin = userSearchParameters.get("User Login");
		
		UserClient userClient = null;
		
		try {
			userClient = new UserClient(client);
		} catch (tcAPIException e1) {
			e1.printStackTrace();
		} catch (OIMNotInitializedException e1) {
			e1.printStackTrace();
		}
					
		long userKey = 0L;
		try {
			userKey = OIMUtils.getUserKey(client.getUserOperationsIntf(), userLogin);
		} catch (Exception e) {
		}
		if (userKey == 0L) {
			return null;
		}
		ArrayList<FieldOriginResult> requestFieldOriginResults = new ArrayList<FieldOriginResult>();
		ArrayList<SearchResults> requestAttributes = getRequestAttributes(requestSearchParameters, keys.get(2),
				null, userKey, keys.get(0), keys.get(1));
		for (SearchResults searchResult : requestAttributes) {
//			FieldOriginResult requestFieldOriginResult = new FieldOriginResult(keys.get(2), keys.get(0) + "." + keys.get(1),
//					searchResult.attributes, null, searchResult.key, null);
//			requestFieldOriginResults.add(requestFieldOriginResult);
		}
		return requestFieldOriginResults;
	}

	
	public ArrayList<SearchResults> getRequestAttributes(HashMap<String, String> requestSearchParameters)
			throws AccessDeniedException, RequestServiceException, NoRequestPermissionException {
		RequestService rs = getRequestService();
		String requestKey = null;

		if (requestSearchParameters.containsKey(RequestConstants.REQUEST_KEY)) {
			requestKey = requestSearchParameters.get(RequestConstants.REQUEST_KEY);
		}

		if (requestKey == null) {
			throw new RequestServiceException("No request key provided");
		}

		Request request = rs.getBasicRequestData(requestKey);

		if (request == null) {
			throw new RequestServiceException("No request found");
		}
		Object result = null;
		HashMap<String, Object> attributes = new HashMap<String, Object>();

		SearchResults searchResult = new SearchResults("RequestData", requestKey, attributes);
		ArrayList<SearchResults> requestAttributes = new ArrayList<SearchResults>();
		requestAttributes.add(searchResult);
		return requestAttributes;
	}

	public ArrayList<SearchResults> getRequestAttributes(HashMap<String, String> requestSearchParameters,
			String attributeType, String attributeName, long userKey, String formName, String childAttributeName)
					throws RequestServiceException, AccessDeniedException, NoRequestPermissionException {
		RequestService rs = getRequestService();
		String requestKey = null;

		if (requestSearchParameters.containsKey(RequestConstants.REQUEST_KEY)) {
			requestKey = requestSearchParameters.get(RequestConstants.REQUEST_KEY);
		}

		if (requestKey == null) {
			throw new RequestServiceException("No request key provided");
		}

		// HashMap <String, String> objSearchMap = new HashMap <String, String>
		// ();
		// objSearchMap.put(REQUEST_ID_ATTR, requestKey);

		Request request = rs.getBasicRequestData(requestKey);

		if (request == null) {
			throw new RequestServiceException("No request found");
		}
		Object result = null;
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (attributeType.equals("REQUEST_OBJECT")) {
			if (attributeName.equals(RequestConstants.REQUEST_END_DATE)) {
				result = request.getEndDate();
			} else if (attributeName.equals(RequestConstants.REQUEST_CREATION_DATE)) {
				result = request.getCreationDate();
			} else if (attributeName.equals(RequestConstants.REQUEST_ID)) {
				result = request.getRequestID();
			} else if (attributeName.equals(RequestConstants.REQUEST_STATUS)) {
				result = request.getRequestStatus();
			} else if (attributeName.equals(RequestConstants.REQUEST_STATE)) {
				result = request.getRequestStage();
			} else if (attributeName.equals(RequestConstants.REQUESTER_KEY)) {
				result = request.getRequesterKey();
			}
			attributes.put(attributeName, result);
		} else if (attributeType.equals("FORM_FIELD")) {
			ArrayList <String> requestFormFieldsMap = getRequestFormFieldsMap(request, userKey, formName, attributeName);
			if (requestFormFieldsMap != null && requestFormFieldsMap.size() > 0) {
				attributes.put(attributeName, requestFormFieldsMap.get(0));
			}
		} else if (attributeType.equals("CHILD_FORM_FIELD")) {
			ArrayList <String> requestFormFieldsMap = getRequestFormFieldsMap(request, userKey, formName, childAttributeName);
			for (int index = 0; index < requestFormFieldsMap.size(); index++) {
				attributes.put(childAttributeName + "_" + index, requestFormFieldsMap.get(index));
			}
		}
		SearchResults searchResult = new SearchResults("RequestData", requestKey, attributes);
		ArrayList<SearchResults> requestAttributes = new ArrayList<SearchResults>();
		requestAttributes.add(searchResult);
		return requestAttributes;
	}

	public ArrayList <String> getRequestFormFieldsMap(Request request, long userKey, String formName, String targetAttributeName) {
		String attrName = null;
		String attrValue = null;
		List<Beneficiary> beneficiaries = null;
		List<RequestBeneficiaryEntity> rbes = null;
		List<RequestBeneficiaryEntityAttribute> rbeas = null;
		ArrayList<String> fieldMap = new ArrayList <String>();
		beneficiaries = request.getBeneficiaries();
		if (beneficiaries != null) {
			for (Beneficiary beneficiary : beneficiaries) {
				String beneficiaryKey = beneficiary.getBeneficiaryKey();
				String userKeyString = Long.toString(userKey, 0);
				if (userKey != 0L && !userKeyString .equals(beneficiaryKey)) {
					LOGGER.info("BeneficiaryKey: " + beneficiaryKey + " not equal to userKey: " + userKey);
					continue;
				}
				rbes = beneficiary.getTargetEntities();
				for (RequestBeneficiaryEntity rbe : rbes) {
					String entitySubType = rbe.getEntitySubType();
					if (formName != null && !formName.equals(entitySubType)) {
						continue;
					}
					rbeas = rbe.getEntityData();
					for (RequestBeneficiaryEntityAttribute rbea : rbeas) {
						attrName = rbea.getName();
						if (attrName != null && !attrName.equalsIgnoreCase("") && attrName.equalsIgnoreCase(targetAttributeName)) {
							// attrValue = (String)rbea.getValue(); // cannot be
							// used for non-string objects like Boolean
							if (rbea.getValue() == null) { // few attributes
								List<RequestBeneficiaryEntityAttribute> requestBeneficiaryEntityAttributeList = rbea.getChildAttributes();	
								for (RequestBeneficiaryEntityAttribute rbea1: requestBeneficiaryEntityAttributeList) {
//									String childValue = rbea1.getValue().toString();
//									if (childValue != null && !"null".equals(childValue)) {
										fieldMap.add(rbea1.getValue().toString());
//									}
								}
							} else {
								attrValue = rbea.getValue().toString();
								fieldMap.add(attrValue);
							}
						} 
					}
				}
			}
		}
		return fieldMap;
	}

	protected RequestBeneficiaryEntityAttribute getAttr(String name, String value) {
		RequestBeneficiaryEntityAttribute attr = null;

		attr = new RequestBeneficiaryEntityAttribute(name, value, RequestBeneficiaryEntityAttribute.TYPE.String);

		return attr;
	}

	protected RequestBeneficiaryEntityAttribute getAttr(String name, Long value) {
		RequestBeneficiaryEntityAttribute attr = null;

		attr = new RequestBeneficiaryEntityAttribute(name, value, RequestBeneficiaryEntityAttribute.TYPE.Long);

		return attr;
	}

	public Catalog getCatalog(String searchTag, String category) throws Exception {
		Catalog catalog = null;
		CatalogService catalogService = null;
		CatalogSearchCriteria csc1 = null;
		CatalogSearchCriteria csc2 = null;
		CatalogSearchCriteria csc = null;
		CatalogSearchResult searchResults = null;
		List<Catalog> catalogs = null;

		csc1 = new CatalogSearchCriteria(CatalogSearchCriteria.Argument.TAG, searchTag,
				CatalogSearchCriteria.Operator.EQUAL);
		csc2 = new CatalogSearchCriteria(CatalogSearchCriteria.Argument.CATEGORY, category,
				CatalogSearchCriteria.Operator.EQUAL);
		csc = new CatalogSearchCriteria(csc1, csc2, CatalogSearchCriteria.Operator.AND);

		catalogService = client.getOIMClient().getService(CatalogService.class);

		searchResults = catalogService.search(csc, 1, 10, "CATALOG_ID", CatalogSearchCriteria.SortCriteria.ASCENDING);

		catalogs = searchResults.getCatalogs();

		/*
		 * The catalogs List should only contain one item (based on the search
		 * criteria)
		 */

		if (catalogs != null && catalogs.size() == 1) {
			catalog = catalogs.get(0);
			if (catalog == null) {
				throw new Exception("Catalog Item is null, for searchTag '" + searchTag + "'");
			}
		} else {
			throw new Exception("Catalog List is null or has more than one item");
		}

		return catalog;
	}
}
