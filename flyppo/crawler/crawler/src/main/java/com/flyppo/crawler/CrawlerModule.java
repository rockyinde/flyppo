package com.flyppo.crawler;

import com.couchbase.client.java.Bucket;
import com.flyppo.cb.provider.CouchbaseProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CrawlerModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(Bucket.class).toProvider(CouchbaseProvider.class).in(Singleton.class);
	}
}
