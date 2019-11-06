package com.leatop.bee.web.conf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import com.leatop.bee.web.util.JDBCUtils;
import com.leatop.bee.web.util.KafkaConnectorsUtils;

/**
 * kafka图片消费者相关配置
 * 
 * @author hongSheng
 */
//@Configuration
//@EnableKafka
public class KafkaConsumerConfig {
	
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
	@Value("${spring.kafka.consumer.group-id}") 
	private String groupId;
	@Value("${spring.kafka.consumer.properties.max.partition.fetch.bytes}") 
	private String maxPartitionFetchBytes;
	@Value("${spring.kafka.consumer.max-poll-records}") 
	private String maxPollRecords;
	@Value("${spring.kafka.listener.concurrency}") 
	private String concurrency;
	
	
	@Value("${spring.kafka.connectors.connector-url}") 
	private String connectorUrl;
	/*
	 * @Value("${spring.kafka.consumer.enable-auto-commit}") private String
	 * enableAutoCommit;
	 * 
	 * @Value("${spring.kafka.consumer.auto-commit-interval}") private String
	 * autoCommitInterval;
	 * 
	 * @Value("${spring.kafka.consumer.session.timeout.ms}") private String
	 * sessionTimeoutMs;
	 */
	@Value("${bee.web.etc-pass-list-topic}") 
	private String etcPassListTopic;
	
	@Value("${bee.web.etc-trade-list-topic}") 
	private String etcTradeListTopic;
	
	@Value("${bee.web.image-list-topic}") 
	private String imageListTopic;
	
	@Value("${bee.web.image-image-list-topic}") 
	private String imageImageListTopic;
	
	
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(Integer.valueOf(concurrency));
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }
 
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
 
 
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		
		propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		propsMap.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
		//每个批次获取记录条数
		//propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		//设置kafka中消费者消息的消费时间
		propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 7000);
		//设置是否自动提交
		//propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		//propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
		propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,7000);
		propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,7000);
		//propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,sessionTimeoutMs);
		 
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        return propsMap;
    }
    
    
 
    @Bean
    public KafkaMessageListener listener() {
        return new KafkaMessageListener();
    }
    

	public class KafkaMessageListener {
	   private final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageListener.class);
	   
	   private List<Map<String,String>> imageListConf;
	   
	   private int countNUM=0;
	   
	   public void setImageListConf() {
	    	List<String> all_connectors_task = KafkaConnectorsUtils.getAllMatchTopic(connectorUrl);
	    	this.imageListConf = KafkaConnectorsUtils.getAllToWrite(connectorUrl,imageListTopic,all_connectors_task);
		}
	   
	   /**
	    * 	监听kafka中执行主题的消费（处理图片的消费监听）
	    * 
	    * @param records
	    */
	   @KafkaListener(topics = "${bee.web.image-image-list-topic}")
	   public void linstenOnImageListBatch(List<ConsumerRecord<?, byte[]>> records){
		   LOGGER.info("Id0 Listener, Thread ID: {}" ,Thread.currentThread().getId());
	       LOGGER.info("Id0 records size:{}",records.size());
	       if(imageListConf==null) {setImageListConf();}
	       if(records.size()>0) {
	    	   countNUM = countNUM + records.size();
	    	   LOGGER.info("countNUM records size:{}",countNUM);
		       JDBCUtils.writeJDBCBatchQueue(imageListConf,records);
		   }
	   }
	   
	}
	
}