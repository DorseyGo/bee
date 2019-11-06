/**
 * File: KafkaConfiguration.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Dorsey
 *
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "bee.data.weave.kafka")
@PropertySource(encoding = "UTF-8", value = "classpath:bee-data-weave-kafka.properties")
public class KafkaConfiguration {

	private KafkaProducerConfiguration producer;

	/**
	 * @return the producer
	 */
	public KafkaProducerConfiguration getProducer() {
		return producer;
	}

	/**
	 * @param producer
	 *            the producer to set
	 */
	public void setProducer(final KafkaProducerConfiguration producer) {
		this.producer = producer;
	}

	public static class KafkaProducerConfiguration {

		private String messageConverter;
		private long timeoutMs;

		/**
		 * @return the messageSerializer
		 */
		public String getMessageConverter() {
			return messageConverter;
		}

		/**
		 * @param messageConverter
		 *            the messageSerializer to set
		 */
		public void setMessageConverter(final String messageConverter) {
			this.messageConverter = messageConverter;
		}

		/**
		 * @return the timeoutMs
		 */
		public long getTimeoutMs() {
			return timeoutMs;
		}

		/**
		 * @param timeoutMs
		 *            the timeoutMs to set
		 */
		public void setTimeoutMs(final long timeoutMs) {
			this.timeoutMs = timeoutMs;
		}

	}
}
