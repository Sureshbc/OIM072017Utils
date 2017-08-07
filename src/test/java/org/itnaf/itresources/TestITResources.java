package org.itnaf.itresources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import org.itnaf.provisioning.Init;
import org.itnaf.utils.Client;
import org.itnaf.utils.ITResourceUtilities2;
import org.itnaf.utils.UserClient;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import oracle.iam.api.OIMService;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.OIMClient;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningService;

public class TestITResources {
	private static String TARGET = "OIAM11G";
	private static OIMClient oimClient = null;
	private static Client client = null;
	private static String  SOAP = "<soap:address location=\"";

	public TestITResources() throws Exception {
		TARGET = "OIAM11GMAC";
		TARGET = "AWS";
		TARGET = "OIAM11G";

		oimClient = Init.init(TARGET);

		if (oimClient == null) {
			System.out.println("OIMClient is null");
			System.exit(0);
		}

		client = new Client(oimClient);
	}

	public static void main(String[] args) throws Exception {
		TestITResources testITResources = new TestITResources();
		testITResources.testFindITResource();
		System.exit(0);
	}

	public boolean testFindITResource() throws Exception {
		UserClient userClient = new UserClient(client);

		ApplicationInstanceService aiSvc = oimClient.getService(ApplicationInstanceService.class);
		ProvisioningService provSvc = oimClient.getService(ProvisioningService.class);
		UserManager usrMgr = oimClient.getService(UserManager.class);
		tcITResourceInstanceOperationsIntf tcITResourceIntf = oimClient
				.getService(tcITResourceInstanceOperationsIntf.class);
		tcObjectOperationsIntf resourceService = oimClient.getService(tcObjectOperationsIntf.class);
		OIMService unifiedService = oimClient.getService(OIMService.class);

		long[] itResKeys = ITResourceUtilities2.findITResourceInstancesByITResourceTypeName(tcITResourceIntf,
				"SayHello");

		for (int i = 0; i < itResKeys.length; i++) {
			System.out.println("Getting values for it resource " + itResKeys[i]);
			Hashtable[] params = ITResourceUtilities2.getITResourceInstanceParameters(tcITResourceIntf, itResKeys[i]);
			for (int j = 0; j < params.length; j++) {
				// System.out.println(params[j]);
				if (params[j].containsKey("IT Resources Type Parameter.Name")) {
					String key = (String) params[j].get("IT Resources Type Parameter.Name");
					if (key.equals("soaServiceWSDL")) {
						String value = (String) params[j].get("IT Resource.Parameter.Value");

						System.out.println("Testing URL " + value);
						String urlStr = value;
						URL url = null;
						URLConnection urlConnection = null;
						try {
							url = new URL(urlStr);
							urlConnection = url.openConnection();
							if (urlConnection.getContent() != null) {
								System.out.println("GOOD URL");
/*
								URLConnection conn = url.openConnection();

								// open the stream and put it into BufferedReader
								BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

								String inputLine;
								while ((inputLine = br.readLine()) != null) {
									if (inputLine.trim().startsWith(SOAP)) {
										inputLine = inputLine.trim();
										System.out.println(inputLine);
										String newInputLine = inputLine.substring(SOAP.length(), inputLine.length() - 2);
										System.out.println(newInputLine);
										
									}
									System.out.println(inputLine);
								}
								br.close();*/
								
								System.out.println("Testing URL " + "http://host01b:7101/TestApp-Single-context-root/SayHelloPort?wsdl");
								url = new URL("http://host01b:7101/TestApp-Single-context-root/SayHelloPort?wsdl");
								urlConnection = url.openConnection();
								if (urlConnection.getContent() != null) {
									System.out.println("GOOD URL");
								} else {
									System.out.println("BAD URL");
									return false;
								}

								System.out.println("Done");
							} else {
								System.out.println("BAD URL");
								return false;
							}
						} catch (MalformedURLException ex) {
							System.out.println("bad URL");
							return false;
						} catch (IOException ex) {
							System.out.println("Failed opening connection. Perhaps WS is not up?");
							return false;
						}
					}
				}
			}

		}

		return true;
	}
	
	public void exit() {
		oimClient.logout();
		oimClient = null;
		client = null;
		//System.exit(0);
	}

}