package org.itnaf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.itnaf.metadata.utils.FieldOriginResult;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

public class ProcessFormClient {
	final static Logger LOGGER = Logger.getLogger(ProcessFormClient.class.getName());
	private Client client = null;
	private tcFormInstanceOperationsIntf formInstanceOperationsIntf;
	private tcFormDefinitionOperationsIntf formDefinitionOperationsIntf;
	private static final String CHILD_FORM_DEF_KEY = "Structure Utility.Child Tables.Child Key";
	private static final String CHILD_FORM_VERSION_CS = "Structure Utility.Child Tables.Child Version";
	private static final String CHILD_FORM_TABLE_NAME = "Structure Utility.Table Name";

	public ProcessFormClient(Client client) {
		this.client = client;
		formInstanceOperationsIntf = client.getFormInstanceOperationsIntf();
		formDefinitionOperationsIntf = client.getFormDefinitionOperationsIntf();
	}

	/**
	 * 
	 * @param attributeNames
	 * @param fieldOriginResult
	 * @param processInstanceKey
	 * @return
	 */
	public FieldOriginResult getProcessFormFields(ArrayList <ArrayList <String>> attributeNames, 
			FieldOriginResult fieldOriginResult, long processInstanceKey) {
		int numberToFetch = attributeNames.size();
		
		try {
			tcResultSet formInstResults = 
					client.getFormInstanceOperationsIntf().getProcessFormData(processInstanceKey);
			
			String[] columnNames = formInstResults.getColumnNames();
			
			String tableName = null;
			
			// _ROWVER is a column available in all OIM form tables
			for (int index =0; index < columnNames.length; index++) {
				if (columnNames[index].indexOf("_ROWVER") > 0) {
					tableName = columnNames[index];
					int tableNameLength = tableName.indexOf("_ROWVER");
					tableName = tableName.substring(0, tableNameLength + 1);
					break;
				}
			}

			HashMap<String, String> existingValues = fieldOriginResult.getValues();
			for (int fetching = 0; fetching < numberToFetch; fetching++) {
				for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
					String attributeName = attributeNames.get(fetching).get(0);
					if ((tableName + attributeName).equalsIgnoreCase(columnNames[columnIndex])) {
						String value = formInstResults.getStringValueFromColumn(columnIndex);
						existingValues.put("PF." + attributeName, value);
						break;
					}
				}
			}
			fieldOriginResult.setValues(existingValues);
			return fieldOriginResult;
		} catch (Exception e) {
			return fieldOriginResult;
		}
	}

	public FieldOriginResult getUDFChildAttributes(ArrayList<ArrayList<String>> attributeNames,
			FieldOriginResult fieldOriginResult, long processInstanceKey) {

		long childFormKey = findChildFormDefKey(processInstanceKey);
		tcResultSet formDefChildResults;

		int numberToFetch = attributeNames.size();

		try {

			formDefChildResults = formDefinitionOperationsIntf.getFormFields(childFormKey,
					findChildFormVersion(processInstanceKey));
			tcResultSet formInstChildResults = formInstanceOperationsIntf.getProcessFormChildData(childFormKey,
					processInstanceKey);
			int formCount = formInstChildResults.getRowCount();

			HashMap <String, String> existingValues = fieldOriginResult.getValues();
			// iterate the process form child table rows
			if (formCount > 0) {

				String[] columnNames = formInstChildResults.getColumnNames();

				String childTableName = null;
				for (int index =0; index < columnNames.length; index++) {
					if (columnNames[index].indexOf("_ROWVER") > 0) {
						childTableName = columnNames[index];
						int tableNameLength = childTableName.indexOf("_ROWVER");
						childTableName = childTableName.substring(0, tableNameLength + 1);
						break;
					}
				}

				for (int i = 0; i < formCount; i++) {
					formInstChildResults.goToRow(i);

					// iterate the columns on the form child table row
					for (int fetching = 0; fetching < numberToFetch; fetching++) {
						// fieldname is 0
						String attributeName = attributeNames.get(fetching).get(0);
						String value = formInstChildResults.getStringValue((childTableName + attributeName));
						existingValues.put("[PCF." + attributeName+ "]" + formInstChildResults.getStringValue(childTableName + "KEY"), value);
					}
				}

			}

			fieldOriginResult.setValues(existingValues);


		} catch (Exception e) {
		}
		return fieldOriginResult;
	}

	@SuppressWarnings("deprecation")
	public int findChildFormVersion(long procInstanceKey) {
		long formDefKey;
		try {
			formDefKey = formInstanceOperationsIntf.getProcessFormDefinitionKey(procInstanceKey);

			tcResultSet formDefChildResults = formInstanceOperationsIntf.getChildFormDefinition(formDefKey,
					formInstanceOperationsIntf.getProcessFormVersion(procInstanceKey));
			return formDefChildResults.getIntValue(CHILD_FORM_VERSION_CS);
		} catch (Exception e) {
			LOGGER.severe("Error getting child table " + e);
		}

		return 0;
	}

	@SuppressWarnings("deprecation")
	public long findChildFormDefKey(long procInstanceKey) {
		long formDefKey;
		try {
			formDefKey = formInstanceOperationsIntf.getProcessFormDefinitionKey(procInstanceKey);
			tcResultSet formDefChildResults = formInstanceOperationsIntf.getChildFormDefinition(formDefKey,
					formInstanceOperationsIntf.getProcessFormVersion(procInstanceKey));
			formDefChildResults.goToRow(0);;
			return formDefChildResults.getLongValue(CHILD_FORM_DEF_KEY);
		} catch (Exception e) {
			LOGGER.severe("Error getting child table " + e);
		}
		return 0;
	}
}
