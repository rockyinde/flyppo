package com.flyppo.crawler.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.flyppo.crawler.config.CrawlerConfiguration;
import com.flyppo.crawler.config.KafkaConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class QueueService {

	CrawlerConfiguration config;
	Queue<String> queue;
	Producer<String, String> producer;
	Consumer<String, String> consumer;
	
	@Inject
	public QueueService(CrawlerConfiguration config) {
		
		this.config = config;
		this.queue = new LinkedList<String>();
		this.producer = new KafkaProducer<>(getKafkaProperties(config.getKafka()));

		consumer = new KafkaConsumer<>(getKafkaConsumerProperties(config.getKafka()));
		consumer.subscribe(Arrays.asList(config.getKafka().getTopic()));
	}
	
	public void add (String url) {
		
		producer.send(new ProducerRecord<String, String>(config.getKafka().getTopic(), url));
	}
	
	public boolean isEmpty() {
		
		if (queue.isEmpty())
			loadBufferFromKafka();
		
		return queue.isEmpty();
	}
	
	/**
	 * loads the local buffer with new URLs from kafka
	 */
	private void loadBufferFromKafka() {
		
		ConsumerRecords<String, String> records = null;
		
		while (records == null || records.isEmpty()) {

			records = consumer.poll(100);
			log.info("fetched {} records from kafka", records.count());

			if (records.isEmpty()) {
				
				log.error("empty records fetched from kafka");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					log.error("thread interrupted from sleep");
				}
			} else
				consumer.commitSync();
		}
		
		ConsumerRecord<String, String> topRecord = null;
		int i = 0;
		for (ConsumerRecord<String, String> record : records) {
			
			queue.add(record.value());
			
			if (i == 0)
				topRecord = record;
			i++;
		}
		
		log.info("latest offset fetched from kafka: {}, value is {}, key is {}", topRecord.offset(), topRecord.value(), topRecord.key());
	}
	
	public String poll() {

		if (queue.isEmpty())
			loadBufferFromKafka();
		
		return queue.poll();
	}
	
	public int size() {
		return queue.size();
	}
	
	private static String getBrokerUrl (KafkaConfiguration kafkaConfig) {
		
		return kafkaConfig.getUrl()+":"+kafkaConfig.getPort();
	}
	
	private static Properties getKafkaProperties(KafkaConfiguration kafkaConfig) {
		
		 Properties props = new Properties();
		 props.put("bootstrap.servers", getBrokerUrl(kafkaConfig));
		 props.put("acks", "all");
		 props.put("retries", 0);
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");	
	
		 return props;
	}
	
	private static Properties getKafkaConsumerProperties(KafkaConfiguration config) {

	     Properties props = new Properties();
		 props.put("bootstrap.servers", getBrokerUrl(config));
	     props.put("group.id", "g1");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
//	     props.put("auto.offset.reset", "earliest");
	     props.put("session.timeout.ms", "30000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     
	     return props;
	}
}
