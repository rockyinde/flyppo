package com.flyppo.crawler.helper;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.flyppo.crawler.model.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrawlerHelper {

	private static String[] FILE_ENDINGS = {
			"pdf",
			"ppt",
			"pptx",
			"doc",
			"docx",
			"xls",
			"xlsx"
	};

	public static boolean isFile (String url) {
		
		if (StringUtils.isEmpty(url))
			return false;
		
		for (String ending : FILE_ENDINGS)
			if (url.endsWith(ending))
				return true;
		
		return false;
	}
	
	public static boolean isDomain (String domain, String url) {
		
		return url.startsWith(domain);
	}
	
    public static void print(String msg, Object... args) {
        log.info(String.format(msg, args));
    }

    public static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    public static Result compose (Element e) {
    	
		Result r = new Result();
		r.setName(e.text());
		r.setUrl(e.attr("abs:href"));
		r.setText(e.parent().text());

		return r;
    }

	public static Elements getReferences (String url) throws IOException {
		
        Document doc = Jsoup.connect(url).get();
        return doc.select("a[href]");
	}
}
