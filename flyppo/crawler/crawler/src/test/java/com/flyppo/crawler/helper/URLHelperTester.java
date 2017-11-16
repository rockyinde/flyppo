package com.flyppo.crawler.helper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class URLHelperTester {

	@Test
	public void testBaseUri() throws IOException {
		
		String url = "http://www.google.com";
		
        Document doc = Jsoup.connect(url).get();
		String baseUri = doc.baseUri();
		
		log.info("base uri: {}", baseUri);
		log.info("title: {}", doc.title());
	}
	
	String[] trueArr = {
			
			"http://pages.cs.wisc.edu/~karu/courses/cs752/fall2010/wiki/index.php?n=Conference.Conference",
			"http://pages.cs.wisc.edu/~menon"
	};
	
	String[] failArr = {
			
			"http://www.google.com",
			"http://www.google.educom",
			"http://pages.cs.wiscedu.com/~menon",
			"http://www.goedu.com"
	};

	@Test
	public void testIsAcademic() {
	
		for (String url : trueArr)
			Assert.assertTrue(URLHelper.isAcademic(url));

		for (String url : failArr)
			Assert.assertFalse(URLHelper.isAcademic(url));
	}
}
