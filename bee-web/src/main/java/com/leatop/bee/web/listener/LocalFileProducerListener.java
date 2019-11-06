/**
 * File: JDBCProducerListener.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月30日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.listener;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.ProducerListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leatop.bee.common.utils.FileUtils;
import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.domain.RecordCounter;

/**
 * An implementation of {@link LocalFileProducerListener}, which record the
 * errors into the local file for later processing.
 * 
 * @author Dorsey
 *
 */
public class LocalFileProducerListener implements ProducerListener<String, Object> {

	private final ObjectMapper mapper = new ObjectMapper();
	/** path which stores the errors */
	private final String storagePath;
	private static final String ERROR_FILE_SUFFIX = ".json";
	private static final String DEFAULT_STORAGE_PATH = "/tmp";
	private static final Logger log = LoggerFactory.getLogger(LocalFileProducerListener.class);
	private final RecordCounter recordCounter;

	@Autowired
	public LocalFileProducerListener(final BeeWebConfiguration config) {
		this.storagePath = config.getErrorStoragePath();
		this.recordCounter = RecordCounter.getInstance();
	}

	@Override
	public void onSuccess(final String topic, final Integer partition, final String key,
			final Object value, final RecordMetadata recordMetadata) {
		recordCounter.increSuccessCount(topic);
		log.info("Right now #{} records push to topic: {} in KAFKA", recordCounter.getSuccessCount(topic), topic);
	}

	@Override
	public void onError(final String topic, final Integer partition, final String key,
			final Object value, final Exception exception) {
		recordCounter.increFailedCount(topic);
		log.info("Failed: #{} records push to topic: {} in KAFKA", recordCounter.getSuccessCount(topic), topic);
		
		String dest = mkdirIfAbsent();
		File target = new File(dest, topic + ERROR_FILE_SUFFIX);
		RandomAccessFile raf = null;
		String record = null;
		try {
			if (!target.exists()) {
				target.createNewFile();
			}
			
			raf = new RandomAccessFile(target, "rw");
			long curPos = raf.length();
			raf.seek(curPos);
			
//			record = mapper.writeValueAsString(value);
			record = value.toString();
			raf.write(record.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			// only log down this error
			log.warn("IO error detected when writing [{}] to file:{}", record, target, e);
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException ignore) {
					// ignore this exception.
				}
			}
		}
	}

	private String mkdirIfAbsent() {
		String path = storagePath == null ? DEFAULT_STORAGE_PATH : storagePath;
		FileUtils.mkdirIfAbsent(path);

		return path;
	}

	@Override
	public boolean isInterestedInSuccess() {
		return true;
	}
	
	
	/**
	 * @return the recordCounter
	 */
	public RecordCounter getRecordCounter() {
		return recordCounter;
	}
}
