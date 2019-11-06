package com.leatop.bee.management.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leatop.bee.management.conf.KafkaConfiguration;
import com.leatop.bee.management.page.Pagination;
import com.leatop.bee.management.po.TopicEntity;
import com.leatop.bee.management.resp.Resp;

import kafka.admin.AdminUtils;
import kafka.admin.BrokerMetadata;
import kafka.utils.ZkUtils;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;
import scala.collection.Seq;

/**
*	 操作 Kafka Topic 的工具类
* 
* @author hongSheng，zlm
*
*/
public class KafkaUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUtils.class);
	
    /**
     * 获取ZkUtils
     */
    private static ZkUtils getZkUtils(String zookeeperConnect, int sessionTimeout, int connectTimeout) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zookeeperConnect, sessionTimeout, connectTimeout, JaasUtils.isZkSecurityEnabled());
        } catch (Exception e) {
        	LOGGER.error("initialize zkUtils failed!");
            e.printStackTrace();
        }
        return zkUtils;
    }
    
    /**
            创建主题
    kafka-topics.sh --zookeeper localhost:2181 --create
    --topic kafka-action --replication-factor 2 --partitions 3
     */
    public static Resp createTopic(KafkaConfiguration config,TopicEntity entity){
    	Resp resp = Resp.OK;
        ZkUtils zkUtils = null;
        try {
        	zkUtils = getZkUtils(config.getZookeeperHost(), config.getSessionTimeout(), config.getConnectTimeout());
        	LOGGER.info(entity.toString());
            if (!AdminUtils.topicExists(zkUtils,entity.getName())){
                AdminUtils.createTopic(zkUtils,entity.getName(),entity.getPartitions(),
                        entity.getReplicationFactor(),new Properties(),
                        AdminUtils.createTopic$default$6());
            }
            else {
            	resp = Resp.FAILURE.withPhase("主题" + entity.getName()+" 已经存在!");
            }
 
        }catch (Exception e){
            e.printStackTrace();
            resp = Resp.FAILURE.withPhase(e.getMessage());
        }finally {
            if (zkUtils!=null){
                zkUtils.close();
            }
        }
        return resp;
    }
    
    /**
           删除某主题
    kafka-topics.sh --zookeeper localhost:2181 --topic kafka-action --delete
     */
    public static Resp deleteTopic(KafkaConfiguration config,String name){
    	Resp resp = Resp.OK;
    	ZkUtils zkUtils = getZkUtils(config.getZookeeperHost(), config.getSessionTimeout(), config.getConnectTimeout());
        try {
            AdminUtils.deleteTopic(zkUtils, name);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Resp.FAILURE.withPhase(e.getMessage());
        } finally {
            zkUtils.close();
        }
        return resp;
    }
    
    /**
     * 分区副本重分配
     * @param topic
     * @param numTotalPartitions 重分区后的分区数
     * @param numReplicasPerPartition 重分区后每个分区的副本数
     */
    public static Resp reallocateReplica(KafkaConfiguration config,TopicEntity entity) {
    	Resp resp = Resp.OK;
        ZkUtils zkUtils = getZkUtils(config.getZookeeperHost(), config.getSessionTimeout(), config.getConnectTimeout());
        try {
            // 获取broker原数据信息
            scala.collection.Seq<BrokerMetadata> brokerMetadata = AdminUtils.getBrokerMetadatas(zkUtils, 
                    AdminUtils.getBrokerMetadatas$default$2(), 
                    AdminUtils.getBrokerMetadatas$default$3());
            // 生成新的分区副本分配方案
            scala.collection.Map<Object, Seq<Object>> replicaAssign = AdminUtils.assignReplicasToBrokers(brokerMetadata, 
            		entity.getPartitions(), // 分区数
            		entity.getReplicationFactor(), // 每个分区的副本数
                    AdminUtils.assignReplicasToBrokers$default$4(), 
                    AdminUtils.assignReplicasToBrokers$default$5());
            // 修改分区副本分配方案
            AdminUtils.createOrUpdateTopicPartitionAssignmentPathInZK(zkUtils, 
                    entity.getName(), 
                    replicaAssign, 
                    null, 
                    true);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Resp.FAILURE.withPhase(e.getMessage());
        } finally {
            zkUtils.close();
        }
        return resp;
    }
    
    /**
     * 获取主题元数据
     */
    public static List<PartitionInfo> getTopicMetadata(String brokerList, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        consumer.close();
        return partitionInfos;
    }
    
    public static List<String> getAllTopics(String zookeeperConnect, int sessionTimeout, int connectTimeout){
    	List<String> re_topics = new ArrayList<String> ();
    	ZkUtils zkUtils = ZkUtils.apply(zookeeperConnect, sessionTimeout, connectTimeout, JaasUtils.isZkSecurityEnabled());
		Map<String, Properties> topics = JavaConverters.mapAsJavaMapConverter(AdminUtils.fetchAllTopicConfigs(zkUtils))
				.asJava();
		for (Entry<String, Properties> entry : topics.entrySet()) {
			//String key = entry.getKey();
			//Object value = entry.getValue();
			re_topics.add(entry.getKey());
		}
		zkUtils.close();
		return re_topics;
    }
    
    public static Pagination<TopicEntity> getAllTopicsMetadata(String zookeeperConnect, int sessionTimeout, int connectTimeout,String bootstrapServers,int page, int limit){
    	System.out.println("zookeeperConnect地址："+zookeeperConnect);
    	List<TopicEntity> re_topics = new ArrayList<TopicEntity> ();
    	ZkUtils zkUtils = ZkUtils.apply(zookeeperConnect, sessionTimeout, connectTimeout, JaasUtils.isZkSecurityEnabled());
//		Map<String, Properties> topics = JavaConverters.mapAsJavaMapConverter(AdminUtils.fetchAllTopicConfigs(zkUtils))
//				.asJava();
//		for (Entry<String, Properties> entry : topics.entrySet()) {
//			//List<PartitionInfo> partitionInfos = KafkaUtils.getTopicMetadata(bootstrapServers, entry.getKey());
//			TopicEntity topicEntity = new TopicEntity();
//			topicEntity.setName(entry.getKey());
//			//topicEntity.setPartitions(partitionInfos.size());
//			//topicEntity.setReplicationFactor(partitionInfos.get(0).replicas().length);
//			re_topics.add(topicEntity);
//		}
    	List<String> topics = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
    	for(int i=(page-1)*limit;i<(page-1)*limit+limit;i++) {
    		if (i>=topics.size()) {
				break;
			}
    		TopicEntity topicEntity = new TopicEntity();
    		topicEntity.setName(topics.get(i));
    		re_topics.add(topicEntity);
    	}
		zkUtils.close();
		Pagination<TopicEntity> res = new Pagination<>();
		res.setCode(0);
//		res.setCount(topicService.countTopics(params));
		res.setCount(topics.size());
		res.setData(re_topics);
		return res;
    }
    
    
    /**
	 * 	根据kafka-connectors的url获取所有下发任务名称
	 * 
	 * @param kafka_connectors_url
	 * @return
	 */
	public static List<String> getAllMatchTopic(String kafka_connectors_url) {
		List<String> connectorList = null;
		HttpURLConnection connection = null;
		try {
			URL httpUrl = new URL(kafka_connectors_url);
			LOGGER.info("connector的数据接口地址："+kafka_connectors_url);
			connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 得到响应码
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 得到响应流
				InputStream inputStream = connection.getInputStream();
				// 获取响应
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					LOGGER.info(line);
					ObjectMapper mapper = new ObjectMapper();
					connectorList = (List<String>) mapper.readValue(line, Object.class);
				}
				reader.close();
				// 该干的都干完了,记得把连接断了
				connection.disconnect();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.info("exception："+ex.getMessage());
		} finally {
			try {
				connection.disconnect();
			} catch (Exception ex) {
				ex.printStackTrace();
				LOGGER.info("exception："+ex.getMessage());
			}
		}
		return connectorList;
	}
	
	/**
	 * 	获取指定主题中的所有下的数据库信心
	 *  
	 * @param kafka_connectors_url
	 * @param image_topic
	 * @param connectors
	 * @return
	 */
	public static List<Map<String,String>> getAllToWrite(String kafka_connectors_url,String image_topic,List<String> connectors) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(connectors == null ) {
			return list;
		}else {
			for(int i=0;i<connectors.size();i++) {
				HttpURLConnection connection = null;
				try {
					URL httpUrl = new URL(kafka_connectors_url+connectors.get(i));
					LOGGER.info("connector的数据接口地址："+kafka_connectors_url);
					connection = (HttpURLConnection) httpUrl.openConnection();
					connection.setRequestMethod("GET");
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setUseCaches(false);
					// 得到响应码
					int responseCode = connection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						// 得到响应流
						InputStream inputStream = connection.getInputStream();
						// 获取响应
						BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
						String line;
						while ((line = reader.readLine()) != null) {
							LOGGER.info(line);
							ObjectMapper mapper = new ObjectMapper();
							LinkedHashMap<String, Object> linkedHashMap =(LinkedHashMap<String, Object>) mapper.readValue(line, Object.class);
							if(linkedHashMap != null) {
								LinkedHashMap<String, String> config = (LinkedHashMap<String, String>)linkedHashMap.get("config");
								String topic = config.get("topics");
								if(topic.contentEquals(image_topic)) {
									Map<String,String> mapTemp  = new HashMap<String,String>();
									String jdbcURL = config.get("connection.url");
									mapTemp.put("jdbcURL", jdbcURL);
									String jdbcUser = config.get("connection.user");
									mapTemp.put("jdbcUser", jdbcUser);
									String jdbcPasswd = config.get("connection.password");
									mapTemp.put("jdbcPasswd", jdbcPasswd);
									list.add(mapTemp);
								}
							}
						}
						reader.close();
						// 该干的都干完了,记得把连接断了
						connection.disconnect();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					LOGGER.error(ex.getMessage());
				} finally {
					try {
						connection.disconnect();
					} catch (Exception ex) {
						ex.printStackTrace();
						LOGGER.error(ex.getMessage());
					}
				}
				
			}
		}
		return list;
		
	}
}
