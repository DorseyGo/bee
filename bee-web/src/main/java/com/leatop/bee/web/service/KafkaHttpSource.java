/**
 * File: KafkaHttpSource.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.service;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.common.utils.ReflectionUtils;
import com.leatop.bee.data.converter.Converter;
import com.leatop.bee.data.weaver.exception.DataWeaveException;
import com.leatop.bee.data.weaver.source.Source;
import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.conf.KafkaConfiguration;
import com.leatop.bee.web.converter.AvroMessageConverter;
import com.leatop.bee.web.listener.LocalFileProducerListener;

/**
 * @author Dorsey
 *
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KafkaHttpSource implements Source<String, TrafficData> {

	/**
	 * The template engine for interaction with Kafka cluster.
	 */
	private final KafkaTemplate<String, Object> kafkaTemplate;

	private final Converter<SpecificRecord> converter;
	private final KafkaConfiguration config;
	
	/**
	 * Constructor of {@link KafkaHttpSource}, with Kafka template specified.
	 * 
	 * @param kafkaTemplate
	 *            the kafka template.
	 */
	@Autowired
	public KafkaHttpSource(final KafkaTemplate<String, Object> kafkaTemplate,
			final KafkaConfiguration config, final BeeWebConfiguration beeConfig) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaTemplate.setProducerListener(new LocalFileProducerListener(beeConfig));
		this.config = config;

		String converterClazz = this.config.getProducer().getMessageConverter();
		if (converterClazz == null) {
			this.converter = new AvroMessageConverter();
		} else {
			try {
				Class<? extends Converter> clazz = (Class<? extends Converter>) Class
						.forName(converterClazz);
				this.converter = ReflectionUtils.newInstance(clazz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void send(final String topic, final TrafficData data) throws DataWeaveException {
		final SpecificRecord value = this.converter.convertFrom(data);
		this.kafkaTemplate.send(new ProducerRecord<String, Object>(topic, value));
	}

	@Override
	public void send(final String topic, final String key, final TrafficData data)
			throws DataWeaveException {
		final SpecificRecord value = this.converter.convertFrom(data);
		this.kafkaTemplate.send(new ProducerRecord<String, Object>(topic, key, value));
	}

}
