package com.flyppo.crawler.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaConfiguration {

	private String url;
	private int port;
	private String topic;
}
