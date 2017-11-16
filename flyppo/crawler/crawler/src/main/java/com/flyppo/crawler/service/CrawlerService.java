package com.flyppo.crawler.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.flyppo.crawler.helper.CrawlerHelper;
import com.flyppo.crawler.helper.URLHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrawlerService {
	
	@Inject
	QueueService queueService;
	
	@Inject
	CacheService cacheService;
	
	@Inject
	IndexService index;
	
	@Inject
	FilterService filter;
	
	ExecutorService executor = Executors.newFixedThreadPool(1);
	
	/**
	 * processes one hyperreference in the current page
	 * 
	 * @param ref
	 */
	private void processHyperRef (Element ref) {
		
		String refUrl = ref.attr("abs:href");
		refUrl = URLHelper.strip(refUrl);
		
		// if already visited abort
		if (cacheService.contains(refUrl))
			return;
		else
			// add to visited list
			cacheService.add(refUrl);
		
		// apply filters
		if (!filter.pass(refUrl)) {
			
			log.info("filter fail for new URL : {}", refUrl);
			return;
		} else
			log.info("filter PASS for new URL: {}", refUrl);

		if (CrawlerHelper.isFile(refUrl)) {

			index.add(CrawlerHelper.compose(ref));
			log.info("found file: {}", refUrl);
			return;
		}
		else
			queueService.add(refUrl);
	}
	
	/**
	 * picks the next url from the queue
	 * 
	 * @throws IOException
	 */
	private void crawlNext () throws IOException {
		
		// fetch the references in the url
		String currUrl = queueService.poll();
		
		log.info("fetching hyperlinks for {}", currUrl);
		Elements refs = CrawlerHelper.getReferences(currUrl);
		
		// process the hyper refs
		for (Element ref : refs)
			processHyperRef(ref);
	}
	
	/**
	 * starts the crawler
	 * 
	 * @param url
	 * @throws IOException
	 */
	private void crawl () {

		int stats = 0;
		
		// perform iteratively for each url
		while (!queueService.isEmpty()) {
			
			try {
				crawlNext ();
			} catch (IOException e) {
				
				log.error("failed to crawl the last URL, taking up next");
			}
			
			if (stats++ > 10)
			{
				printStats();
				stats = 0;
			}
		}
	}

	private void printStats() {
		
		log.info("files found so far: {}", index.size());
	}
	
    public void start () {
    	
    	executor.submit(() -> crawl ());
    }
}
