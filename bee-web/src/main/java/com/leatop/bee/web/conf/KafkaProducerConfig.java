package com.leatop.bee.web.conf;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
 
/**
 * kafka，图片生产者相关配置
 * 
 * @author hongSheng
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {
	
	public KafkaTemplate<String, byte []> kafkaTemplate;
	
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.producer.properties.max.requst.size}")
    private String maxRequestSize;
    @Value("${spring.kafka.producer.compression-type}")
    private String compressType;
    //@Value("${spring.kafka.producer.retries}")
    //private String retries;
    //@Value("${spring.kafka.producer.batch-size}")
    //private String batchSize;
    //@Value("${spring.kafka.producer.linger.ms}")
    //private String lingerMs;
    //@Value("${spring.kafka.producer.buffer-memory}")
    //private String bufferMemory;
 
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,maxRequestSize);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,compressType);
        //props.put(ProducerConfig.ACKS_CONFIG,acks);
        //props.put(ProducerConfig.RETRIES_CONFIG,retries);
        //props.put(ProducerConfig.BATCH_SIZE_CONFIG,batchSize);
        //props.put(ProducerConfig.LINGER_MS_CONFIG,lingerMs);
        //props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return props;
    }
 
    public ProducerFactory<String, byte[]> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
 
    //@Bean("imageKafkaTemplate")
    public KafkaTemplate<String, byte []> kafkaTemplate() {
		/*
		 * if (kafkaTemplate == null) { kafkaTemplate = new
		 * KafkaTemplate<String, byte []>(producerFactory()); }
		 */
        return new KafkaTemplate<String, byte []>(producerFactory());
    }
}