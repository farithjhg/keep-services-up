package com.wolfsoft.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class MyItemProcessor implements ItemProcessor<String, String> {
	private static final Logger log = LoggerFactory.getLogger(MyItemProcessor.class);

	@Override
	public String process(String status) throws Exception {
		log.info("Converting (" + status + ") into (" + status.toUpperCase() + ")");
		
		return status.toUpperCase();
	}

}
