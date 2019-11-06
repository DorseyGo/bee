/**
 * File: DataWeaveInManager.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.service;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.common.utils.ReflectionUtils;
import com.leatop.bee.data.supervisor.ExceptionHandler;
import com.leatop.bee.data.supervisor.Introspector;
import com.leatop.bee.data.supervisor.exception.DataInvalidException;
import com.leatop.bee.data.supervisor.handler.ExceptionHandlerFactory;
import com.leatop.bee.data.supervisor.introspector.Introspectors;
import com.leatop.bee.data.weaver.exception.DataWeaveException;
import com.leatop.bee.web.conf.DataSupervisorConfiguration;

/**
 * A service class, which serves to push data to Kafka cluster.
 * 
 * @author Dorsey
 *
 */
@Component
public final class DataWeaveInManager {

	// ~~~ fields
	// =====================================================================================
	private final DataSupervisorConfiguration config;
	private final Introspectors introspectors;
	private final KafkaHttpSource producer;
	private static final Logger LOG = LoggerFactory.getLogger(DataWeaveInManager.class);

	// ~~~ constructors
	// =====================================================================================
	/**
	 * Constructor of {@link DataWeaveInManager}, with configuration specified.
	 * 
	 * @param config
	 *            the configuration.
	 */
	@Autowired
	public DataWeaveInManager(final DataSupervisorConfiguration config, final KafkaHttpSource producer) {
		this.config = config;
		this.producer = producer;
		this.introspectors = new Introspectors();
		initialize();
	}

	// ~~~ methods
	// =====================================================================================
	/**
	 * Register all introspectors specified in configuration.
	 */
	private void initialize() {
		if (config != null) {
			final String preProcessors = config.getPreProcessors();
			if (preProcessors != null && preProcessors.trim().isEmpty() == false) {
				final Iterator<String> iter = StringUtils.commaDelimitedListToSet(preProcessors)
						.iterator();

				String preProcessor = null;
				Introspector introspector = null;
				while (iter.hasNext()) {
					preProcessor = iter.next();
					if (preProcessor != null) {
						try {
							Class<?> clazz = Class.forName(preProcessor);
							introspector = (Introspector) ReflectionUtils.newInstance(clazz);
							introspectors.register(introspector);
						} catch (ClassNotFoundException e) {
							LOG.warn("Class: " + preProcessor + " not found");
							continue;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 	推送数据到kafka集群
	 * 
	 * @param topic
	 * @param key
	 * @param data
	 * @return
	 */
	public <ID> boolean weaveIn(final String topic, final String key, final TrafficData<ID> data) {
		try {
			doIntrospect(data);
			doPush(topic, key, data);
		} catch (DataInvalidException e) {
			LOG.warn(e.getMessage(), e);
			return false;
		}

		return true;
	}
	
	/**
	 * 	推送数据到kafka集群
	 * 
	 * @param topic
	 * @param key
	 * @param datas
	 * @return
	 */
	public <ID> boolean weaveIn(final String topic, final String key, final Collection<TrafficData<ID>> datas) {
		boolean succeed = true;
		if (datas != null && datas.isEmpty() == false) {
			for (TrafficData<ID> data : datas) {
				succeed = weaveIn(topic, key, data);
				if (succeed == false) {
					break;
				}
			}
		}
		
		return succeed;
	}
	
	public <ID> boolean weaveIn(final String topic, final Collection<TrafficData<ID>> datas) {
		boolean succeed = true;
		if (datas != null && datas.isEmpty() == false) {
			for (TrafficData<ID> data : datas) {
				succeed = weaveIn(topic, data.getId().toString(), data);
				if (succeed == false) {
					break;
				}
			}
		}
		
		return succeed;
	}

	/**
	 * Push the given <code>data</code> to <tt>kafka</tt> cluster.
	 * 
	 * @param data
	 */
	private <ID> void doPush(final String topic, final String key, final TrafficData<ID> data) {
		try {
			this.producer.send(topic, key, data);
		} catch (DataWeaveException ignore) {
			// there is mechanism to guarantee that the data will be successfully pushed to 
			// Kafka cluster, so no need to propogate this exception anymore.
			LOG.warn("Failed to push data: " + data + " to the topic: " + topic);
		}
	}

	/**
	 * 	对数据进行过滤
	 * 
	 * @param data
	 */
	private <ID> void doIntrospect(final TrafficData<ID> data) throws DataInvalidException {
		try {
			this.introspectors.introspect(data);
		} catch (DataInvalidException e) {

			// process the exception data.
			ExceptionHandler handler = ExceptionHandlerFactory.getHandler(e.getCode());
			handler.handle(data);

			// rethrow it
			throw e;
		}
	}
}
