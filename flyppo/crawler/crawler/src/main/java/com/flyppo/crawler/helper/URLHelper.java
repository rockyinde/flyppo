package com.flyppo.crawler.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLHelper {
	
	public static boolean isAcademic (String url) {
		
		Pattern pattern = Pattern.compile("\\.edu(/\\S*)?\\z");
		Matcher matcher = pattern.matcher(url);
		
		return matcher.find();
	}
	
	public static boolean isValid (String url) {
		
		return !(url == null || url.isEmpty() || url.length() < 5);
	}
	
	/**
	 * strips query params etc-
	 * 
	 * @param url
	 * @return
	 */
	public static String strip (String refUrl) {
		
		// remove request params
		int ind = refUrl.indexOf('?');
		if (ind >= 0)
			refUrl = refUrl.substring(0, ind);
		
		// remove sub sections
		ind = refUrl.indexOf('#');
		if (ind >= 0)
			refUrl = refUrl.substring(0, ind);
		
		return refUrl;
	}
}
