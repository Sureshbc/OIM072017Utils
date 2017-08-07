package org.itnaf.metadata.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtils {
	
	
	public static String processTemplate(HashMap <String, String> entries, String template) {
		String newTemplate = template;
		
		if (template.indexOf("<conditional::") >= 0) {
			newTemplate = processConditionals( entries,  template);
		}
		
		for (String key: entries.keySet()) {
			String regex = "<" + key + ">" + "+";
			Pattern scriptPattern = Pattern.compile(regex);
			Matcher matcher = scriptPattern.matcher(newTemplate);
			newTemplate = matcher.replaceAll(entries.get(key));
		}
		
		return newTemplate;
	}
	
	/**
	 * Remove entries in the HashMap that don't fit the template
	 * 
	 * @param original
	 * @param fields
	 * @param template
	 * @return
	 */
	public static HashMap <String, String> getMatchingOnly(HashMap <String, String> original, 
			ArrayList<String> fields, String template) {
		
		Pattern scriptPattern = ParseUtils.getPattern(fields, template);
		ArrayList <String> keySet = new ArrayList <String>();
		for (String key: original.keySet()) {
			keySet.add(key);
		}
		for (String key: keySet) {			
			Matcher matcher = scriptPattern.matcher(key);
			if (!matcher.matches()) {
				original.remove(key);
			}
		}
		
		return original;
	}
	
	/** 
	 * Return a pattern that will match all the entries we previously added to the OIM field
	 * 
	 * @param fields
	 * @param template
	 * @return
	 */
	public static Pattern getPattern(ArrayList<String> fields, String template) {
		String newTemplate = template;
		
		for (int index = 0; index < fields.size(); index ++) {
			String pattern = "<" + fields.get(index) + ">";
			newTemplate = replaceAll(newTemplate, pattern, "(.+)");
		}

		newTemplate = replaceAll(newTemplate, "{", "\\{");
		newTemplate = replaceAll(newTemplate, "}", "\\}");

		Pattern scriptPattern = Pattern.compile(newTemplate);
		return scriptPattern;
	}
	
	
	private static String replaceAll(String original, String replaceFrom, String replaceTo) {
		int continueIndex = 0;
		
		// Just in case something goes wrong, it will work with while (true) {}
		for (int occurrences = 0; occurrences < 10; occurrences++) {
			int startIndex = original.indexOf(replaceFrom);

			if (startIndex >= continueIndex) {
				int endIndex = startIndex + replaceFrom.length();
				continueIndex = startIndex + replaceTo.length() + 1;
				original = original.substring(0,startIndex) + replaceTo + original.substring(endIndex);
			} else {
				break;
			}
			System.out.println(original);
		}	
		return original;
	}
	
	public static HashMap getMatching(HashMap <String, String> entries, String template) {
		String newTemplate = template;
		
		if (template.indexOf("<conditional::") >= 0) {
			newTemplate = processConditionals( entries,  template);
		}
		
		for (String key: entries.keySet()) {
			String regex = "<" + key + ">" + "+";
			Pattern scriptPattern = Pattern.compile(regex);
			Matcher matcher = scriptPattern.matcher(newTemplate);
			newTemplate = matcher.replaceAll(entries.get(key));
		}
		
		return null;
	}
	

	
	public static String processConditionals(HashMap <String, String> entries, String template) {
		boolean continueReplacing = true;
		while (continueReplacing) {
			continueReplacing = false;
			int position = template.indexOf("<conditional::");
			if (position >=0 ) {
				int endPosition = template.substring(position).indexOf(">");
				System.out.println();
				
			}
			
		}
		return template;
	}
	
	/**
	 * Parse Strings separated by "." 
	 * 
	 * @param rest
	 * @return
	 */
	public static ArrayList<String> parseDotDelimited(String rest) {

		ArrayList<String> result = new ArrayList<String>();

		// Compile the regex.
		String regex = "(\\w|\\s)+\\.|(\\w|\\s)+";
		Pattern pattern = Pattern.compile(regex);

		// Create the 'target' string we wish to interrogate.

		// Get a Matcher based on the target string.

		Matcher matcher = pattern.matcher(rest);

		// Find all the matches.

		int count = matcher.groupCount();

		while (matcher.find()) {
			String match = matcher.group();
			if (match.endsWith(".")) {
				match = match.substring(0, match.length() - 1);
			}
			result.add(match);
		}
		return result;
	}

	/**
	 * Parse delimited Strings
	 * 
	 * @param delims
	 * @param delimitedString
	 * @return
	 */
	public static ArrayList<String> parseDelimitedStrings(String delims, String delimitedString) {

		ArrayList<String> result = new ArrayList<String>();

		String[] tokens = delimitedString.split(delims);
		int tokenCount = tokens.length;
		for (int j = 0; j < tokenCount; j++) {
			String token = tokens[j].trim();
			result.add(token);
		}
		return result;
	}


}
