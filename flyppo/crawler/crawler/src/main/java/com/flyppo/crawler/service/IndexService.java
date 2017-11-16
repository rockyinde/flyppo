package com.flyppo.crawler.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import com.flyppo.crawler.model.Result;

@Singleton
public class IndexService {

	List<Result> resultSet = new ArrayList<>();

	public void add (Result r) {
		
		resultSet.add(r);
	}
	
	public int size() {
		return resultSet.size();
	}
}
