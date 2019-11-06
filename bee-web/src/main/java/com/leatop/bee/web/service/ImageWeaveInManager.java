/**
 * File: DataWeaveInManager.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.leatop.bee.web.conf.KafkaProducerConfig;

/**
 * A service class, which serves to push data to Kafka cluster.
 * 
 * @author hongSheng
 *
 */
@Component
public final class ImageWeaveInManager {

	// ~~~ fields
	// =====================================================================================
	private final KafkaProducerConfig kafkaProducerConfig;
	private final KafkaTemplate<String, byte[]> kafkaTemplate;
	private static final Logger LOG = LoggerFactory.getLogger(ImageWeaveInManager.class);

	// ~~~ constructors
	// =====================================================================================
	/**
	 * Constructor of {@link ImageWeaveInManager}, with configuration specified.
	 * 
	 * @param config
	 *            the configuration.
	 */
	@Autowired
	public ImageWeaveInManager(KafkaProducerConfig kafkaProducerConfig) {
		//this.imageProducer = imageProducer;
		this.kafkaProducerConfig = kafkaProducerConfig;
		this.kafkaTemplate = this.kafkaProducerConfig.kafkaTemplate();
	}
	
	/**
	 * 	推送数据到kafka
	 * 
	 * @param topic
	 * @param key
	 * @param data
	 * @return
	 */
	public <ID> boolean imageIn(final String topic, final String key, final byte[] data) {
		doPush(topic, key, data);
		return true;
	}
	
	/**
	 * Push the given <code>data</code> to <tt>kafka</tt> cluster.
	 * 
	 * @param data
	 */
	private <ID> void doPush(final String topic, final String key, final byte [] data) {
		try {
			
			//this.kafkaProducerConfig.kafkaTemplate().send(topic, key, data);
			this.kafkaTemplate.send(topic, key, data);
			//下面采取的是异步的方式完成消息的发送，发送成功或者失败，都有回调函数进行后续逻辑处理，非常方便
			/**
			ListenableFuture<SendResult<String, byte[]>> future = this.kafkaProducerConfig.kafkaTemplate().send(topic, key, data);
			future.addCallback(null, new ListenableFutureCallback<SendResult<String, byte[]>>() {
	            @Override
	            public void onSuccess(SendResult<String, byte[]> SendResult) {
	                long offset = SendResult.getRecordMetadata().offset();
	                //String cont = SendResult.getRecor.toString();
	                LOG.info(", offset: " + offset);
	            }

	            @Override
	            public void onFailure(Throwable throwable) {
	            	LOG.error(throwable.getMessage());
	            }
	        });
	        **/
		} catch (Exception ignore) {
			// there is mechanism to guarantee that the data will be successfully pushed to 
			// Kafka cluster, so no need to propogate this exception anymore.
			LOG.warn("Failed to push key: " + key + " to the topic: " + topic);
		}
	}

	
}
