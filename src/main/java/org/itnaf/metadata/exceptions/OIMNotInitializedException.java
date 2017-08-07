package org.itnaf.metadata.exceptions;

/**
 * Exception thrown when neither OIMClient nor tcDataProvider are passed
 * 
 * @author Marco Fanti
 *
 */
public class OIMNotInitializedException extends Exception {
	private static final long serialVersionUID = -4527304606792636587L;

	public OIMNotInitializedException(String string) {
		super(string);
	}

}
