package com.flyppo.crawler.service;

import javax.inject.Singleton;

import com.flyppo.crawler.helper.URLHelper;

@Singleton
public class FilterService {

	/**
	 * applies & checks the filters for the url
	 * 
	 * @param url
	 * @return
	 */
	public boolean pass (String url) {
		
		return URLHelper.isValid(url) && URLHelper.isAcademic(url);
	}
}
