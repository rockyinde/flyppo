package com.flyppo.crawler.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.flyppo.crawler.config.CrawlerConfiguration;
import com.flyppo.crawler.service.CrawlerService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/crawl")
public class CrawlerResource {

	@Inject
	CrawlerConfiguration configuration;
	
	@Inject
	CrawlerService crawler;
	
	@GET
	@Path("/ping")
	public String ping () {
		return "pong";
	}

	@GET
	@Path("/url")
	public String url () {
		return configuration.getKafka().getUrl();
	}

	@GET
	@Path("/port")
	public int port () {
		return configuration.getKafka().getPort();
	}
	
	@GET
	@Path("/start")
	public String start() {
		
		crawler.start();
		return "started";
	}
}
