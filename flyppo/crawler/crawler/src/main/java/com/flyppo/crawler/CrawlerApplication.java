package com.flyppo.crawler;

import com.flyppo.crawler.config.CrawlerConfiguration;
import com.flyppo.crawler.resources.CrawlerResource;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CrawlerApplication extends Application<CrawlerConfiguration> {

	private GuiceBundle<CrawlerConfiguration> guiceBundle;

	public static void main(final String[] args) throws Exception {
        new CrawlerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Crawler";
    }

    @Override
    public void initialize(final Bootstrap<CrawlerConfiguration> bootstrap) {
		guiceBundle = GuiceBundle.<CrawlerConfiguration>newBuilder()
				.addModule(new CrawlerModule())
				.setConfigClass(CrawlerConfiguration.class).enableAutoConfig(getClass().getPackage().getName())
				.build(Stage.DEVELOPMENT);
		bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final CrawlerConfiguration configuration,
                    final Environment environment) {
    	environment.jersey().register(new CrawlerResource());
    }
}
