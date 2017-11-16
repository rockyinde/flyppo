package com.flyppo.crawler.db;

import javax.inject.Singleton;

import com.flyppo.cb.dao.CouchbaseGenericDAO;

@Singleton
public class UrlDB extends CouchbaseGenericDAO<String> {

	public UrlDB() {
		
		super(String.class);
	}

	@Override
	public String getDocumentID(String t) {
		
		return t;
	}
}
