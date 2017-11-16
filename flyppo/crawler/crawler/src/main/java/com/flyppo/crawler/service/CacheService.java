package com.flyppo.crawler.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.flyppo.cb.exceptions.DBException;
import com.flyppo.crawler.db.UrlDB;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class CacheService {

	@Inject
	private UrlDB db;
	
	public boolean contains (String url) {
		
		try {

			return StringUtils.isEmpty(db.getOrNull(url));
		} catch (DBException e) {
			
			log.error("error while checking url {}", url, e);
			return false;
		}
	}
	
	public void add (String url) {
		
		try {

			db.save(url);
		} catch (DBException e) {
			
			log.error("error while saving url {}", url, e);
		}
	}
}
