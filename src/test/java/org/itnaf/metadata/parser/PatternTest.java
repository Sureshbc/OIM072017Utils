package org.itnaf.metadata.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itnaf.metadata.utils.ParseUtils;

public class PatternTest {

	public static void main(String[] args) {
		PatternTest pt = new PatternTest();
		pt.test();
	}

	private void test() {
		String template = "{functionalRole:\"Primary Analyst\",region:\"<PCF.CFF1>\",department:\"<PCF.CFF3>\"}";
		ArrayList<String> fields = new ArrayList <String>();
		fields.add("PCF.CFF1");
		fields.add("PCF.CFF3");
		
		
		HashMap <String, String> original = new HashMap <String, String> ();
		original.put("{functionalRole:\"Primary Analyst\",region:\"Value 2\",department:\"Value 4\"}", null);
		original.put("{functionalRole:\"Primary Analysta\",region:\"Value 2\",department:\"Value 4\"}", null);
		Pattern scriptPattern = ParseUtils.getPattern(fields, template);
		String test1 = "{functionalRole:\"Primary Analyst\",region:\"Value 2\",department:\"Value 4\"}";
		Matcher matcher = scriptPattern.matcher(test1);
		System.out.println(matcher.matches() + " :" + test1);
		test1 = "{functionalRole:\"Primary Analysta\",region:\"Value 2\",department:\"Value 4\"}";
		matcher = scriptPattern.matcher(test1);
		System.out.println(matcher.matches() + " :" + test1);
		
		original = ParseUtils.getMatchingOnly(original, fields, template);
		System.out.println("Original: " + original);
		
		System.out.println(ParseUtils.parseDelimitedStrings(",", "PCF.CFF1,PCF.CFF3"));

	}
}
