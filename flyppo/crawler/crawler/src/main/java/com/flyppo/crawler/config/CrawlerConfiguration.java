package com.flyppo.crawler.config;

import com.flyppo.cb.config.CouchbaseConfiguration;

import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlerConfiguration extends Configuration {

	private KafkaConfiguration kafka;
	private CouchbaseConfiguration couchbase;
}
