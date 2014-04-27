package in.retalemine.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {

	private RegExUtil() {
	}

	public static String getCamelCaseString(String input) {
		StringBuffer camelCaseOutput = new StringBuffer();
		Matcher camelCaseMatcher = Pattern.compile("([a-z])([a-z]*)",
				Pattern.CASE_INSENSITIVE).matcher(
				input.trim().replaceAll("\\s+", " "));
		while (camelCaseMatcher.find()) {
			camelCaseMatcher.appendReplacement(camelCaseOutput,
					camelCaseMatcher.group(1).toUpperCase()
							+ camelCaseMatcher.group(2).toLowerCase());
		}
		camelCaseMatcher.appendTail(camelCaseOutput);
		return camelCaseOutput.toString();
	}

	public static String[] resolveProductUnit(String input) {
		String[] result = null;
		StringBuffer productName = null;
		Matcher productNameUnitMatcher = Pattern.compile(
				"([0-9]+)[\\s]*([a-z]+)$", Pattern.CASE_INSENSITIVE).matcher(
				input.trim().replaceAll("\\s*-\\s*", " - "));
		if (productNameUnitMatcher.find()) {
			result = new String[3];
			productName = new StringBuffer();
			result[0] = productNameUnitMatcher.group(1);
			result[1] = productNameUnitMatcher.group(2);
			productNameUnitMatcher.appendReplacement(productName, "");
			result[2] = productName.toString();
			result[2] = result[2].endsWith(" - ") ? result[2].substring(0,
					result[2].length() - 3) : result[2].trim();
		}
		return result;
	}

	public static String[] resolveQuantity(String input) {
		String[] result = null;
		Matcher quantityMatcher = Pattern.compile("(\\s?([0-9]+)(.*))")
				.matcher(input.trim().replaceAll("\\s+", " "));
		if (quantityMatcher.matches() && 3 == quantityMatcher.groupCount()) {
			result = new String[2];
			result[0] = quantityMatcher.group(2);
			result[1] = quantityMatcher.group(3).trim();
		}
		return result;
	}
}
