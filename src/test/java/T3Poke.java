import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class T3Poke {

	public static void main(String[] args) {

		if (args.length < 3) {

			System.out.println("Usage: " + T3Poke.class.getName() + "<protocol> <host> <port> [username] [password]");

			System.exit(1);

		}

		String protocol = args[0];
		String host = args[1];
		String port = args[2];
		String username = "xelsysadm";
		String password = "welcome1";
		if (args.length > 3) {
			username = args[3];
		}
		if (args.length > 4) {
			password = args[4];
		}
		
		try {
			System.out.println("Connecting to " + protocol + "://" + host + ":" + port + " with username " + username );
			Hashtable env = new Hashtable();
			env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
			env.put("java.naming.provider.url", protocol + "://" + host + ":" + port);
			env.put("java.naming.security.principal", username);
			env.put("java.naming.security.credentials", password);
			Context ctx = new InitialContext(env);
			try {
				Object homeObject = ctx.lookup("EmployeeBean");
				// use the EmployeeBean
			} catch (NamingException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					ctx.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("Successfully connected");

		} catch (Exception exception) {

			exception.printStackTrace();

		}

	}

}