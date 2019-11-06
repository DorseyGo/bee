package com.leatop.bee.web.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
*
* get kafka-connectors setting
* 
* @author hongSheng
*
*/
public class KafkaConnectorsUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConnectorsUtils.class);
	
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
