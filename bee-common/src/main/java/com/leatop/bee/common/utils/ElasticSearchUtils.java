/**
 * File: DataWeaveInManager.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leatop.bee.common.domain.TrafficData;

/**
 * ES工具类
 * 
 * @author hongSheng
 *
 */

public class ElasticSearchUtils {

	private static ElasticSearchUtils instance;

	private ElasticSearchUtils() {
	}

	public static synchronized ElasticSearchUtils getInstance() {
		if (instance == null) {
			instance = new ElasticSearchUtils();
		}
		return instance;
	}
	
	// ~~~ fields
	// =====================================================================================
	private static final ElasticSearchConfiguration config;
	private static RestHighLevelClient client;
	private static RestClientBuilder builder;
	private final static ObjectMapper mapper = new ObjectMapper();

	/** The default scheme is "http". */
	public static final String DEFAULT_SCHEME_NAME = "http";
	/** The default port is 9200 */
	public static final int DEFAULT_PORT_NUMBER = 9200;

	private static HttpHost[] HttpHosts = null;

	private static final Logger LOGGER = Logger.getLogger(ElasticSearchUtils.class.getName());

	// ~~~ constructors
	// =====================================================================================
	/**
	 * Constructor of {@link ElasticSearchUtils}, with configuration specified.
	 * 
	 * @param config
	 *            the configuration.
	 */

	static {
		config = new ElasticSearchConfiguration();
		initialize();
		initIndexAndMapping("po_01", "ETCPassList");
		initIndexAndMapping("po_02", "etcTradeList");
		initIndexAndMapping("po_03", "imageList");
	}

	// ~~~ methods
	// =====================================================================================
	/**
	 * Register all introspectors specified in configuration.
	 * 
	 * @return
	 */
	private static void initialize() {
		if (config != null) {
			LOGGER.info("this is config now:");
			LOGGER.info(config.toString());
			final String hosts = config.getHosts();
			final String ports = config.getPorts();
			if (hosts != null && hosts.trim().isEmpty() == false && ports != null
					&& ports.trim().isEmpty() == false) {
				final String[] hostsStr = StringUtils.commaDelimitedListToStringArray(hosts);
				final String[] portsStr = StringUtils.commaDelimitedListToStringArray(ports);
				HttpHosts = new HttpHost[hostsStr.length]; // 实例化数组
				if (hostsStr.length == portsStr.length) {
					for (int i = 0; i < hostsStr.length; i++) {
						String host = hostsStr[i];
						int port = Integer.valueOf(portsStr[i]);
						HttpHost HttpHost = new HttpHost(host, port, DEFAULT_SCHEME_NAME);
						HttpHosts[i] = HttpHost;
					}

				}
			}
			if (hosts != null && hosts.trim().isEmpty() == false && ports == null
					&& ports.trim().isEmpty() == true) {
				final String[] hostsStr = StringUtils.commaDelimitedListToStringArray(hosts);
				HttpHosts = new HttpHost[hostsStr.length]; // 实例化数组
				for (int i = 0; i < hostsStr.length; i++) {
					String host = hostsStr[i];
					HttpHost HttpHost = new HttpHost(host, DEFAULT_PORT_NUMBER,
							DEFAULT_SCHEME_NAME);
					HttpHosts[i] = HttpHost;
				}
			}

			builder = RestClient.builder(HttpHosts);

			final Integer connectTimeOut = config.getConnectTimeOut();
			final Integer socketTimeOut = config.getSocketTimeOut();
			final Integer connectionRequestTimeOut = config.getConnectionRequestTimeOut();

			if (connectTimeOut != null && socketTimeOut != null
					&& connectionRequestTimeOut != null) {
				setConnectTimeOutConfig(connectTimeOut, socketTimeOut, connectionRequestTimeOut);
			}

			final Integer maxConnectNum = config.getMaxConnectNum();
			final Integer maxConnectPerRoute = config.getMaxConnectPerRoute();

			if (maxConnectNum != null && maxConnectPerRoute != null) {
				setMutiConnectConfig(maxConnectNum, maxConnectPerRoute);
			}
			client = new RestHighLevelClient(builder);
		}
	}

	/**
	 * @desc 初始化索引
	 * @param indexName
	 * @param esType
	 * @return
	 */
	public static void initIndexAndMapping(final String indexName, final String esType) {
		if (!checkIndexExist(indexName)) {
			if (createIndex(indexName)) {
				LOGGER.info("create index " + indexName + " success !");
				createMaping(indexName, esType);
			} else {
				LOGGER.info("create index " + indexName + " failed !");
			}
		} else {
			LOGGER.info(" index " + indexName + " exist !");
		}
	}

	/**
	 * @desc 异步httpclient的连接延时配置
	 */
	public static void setConnectTimeOutConfig(final int connectTimeOut, final int socketTimeOut,
			final int connectionRequestTimeOut) {
		builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

			public Builder customizeRequestConfig(final RequestConfig.Builder requestConfigBuilder) {
				requestConfigBuilder.setConnectTimeout(connectTimeOut);
				requestConfigBuilder.setSocketTimeout(socketTimeOut);
				requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
				return requestConfigBuilder;
			}
		});
	}

	/**
	 * @desc 异步httpclient的连接数配置
	 */
	public static void setMutiConnectConfig(final int maxConnectNum, final int maxConnectPerRoute) {
		builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

			public HttpAsyncClientBuilder customizeHttpClient(
					final HttpAsyncClientBuilder httpClientBuilder) {
				httpClientBuilder.setMaxConnTotal(maxConnectNum);
				httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
				return httpClientBuilder;
			}
		});
	}

	/**
	 * @desc 创建索引
	 * @param index
	 * @return
	 */
	public static boolean createIndex(final String index) {
		// index名必须全小写，否则报错
		CreateIndexRequest request = new CreateIndexRequest(index);
		try {
			// 设置分片和备份
			request.settings(Settings.builder().put("index.number_of_shards", 3)
					.put("index.number_of_replicas", 2));
			CreateIndexResponse indexResponse = client.indices().create(request);
			if (indexResponse.isAcknowledged()) {
				LOGGER.info("create index " + index + " success !");
			} else {
				LOGGER.info("create index " + index + " failed !");
			}
			return indexResponse.isAcknowledged();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @desc 插入数据
	 * @param index
	 * @param type
	 * @param object
	 * @return
	 */
	public String addData(final String index, final String type, final String id, final Object object) {
		IndexRequest indexRequest = new IndexRequest(index, type, id);
		try {
			indexRequest.source(mapper.writeValueAsString(object), XContentType.JSON);
			IndexResponse indexResponse = client.index(indexRequest);
			return indexResponse.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * @desc 批量插入数据
     * @param index
     * @param type
     * @param object
     * @return
     */
    public static boolean addDataBatch(String index,String type,int id,List<?> list) {
    	boolean flag = true;
    	try {
    		
	    	BulkRequest request = new BulkRequest();
	    	for(int i=0;i<list.size();i++) {
	    		IndexRequest indexRequest = new IndexRequest(index, type, String.valueOf(id-list.size()+i));
	    		indexRequest.source(mapper.writeValueAsString(list.get(i)), XContentType.JSON);
	    		request.add(indexRequest.source(mapper.writeValueAsString(list.get(i)), XContentType.JSON).opType(DocWriteRequest.OpType.CREATE));
	    	}
	    	request.timeout(TimeValue.timeValueMinutes(2)); 
	    	request.timeout("2m");
	    	
	    	request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); 
	    	request.setRefreshPolicy("wait_for");
	    	//同步方式
			BulkResponse bulkResponse = client.bulk(request,RequestOptions.DEFAULT);
			for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.getFailure() != null) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    LOGGER.info("出现错误：:"+String.valueOf(id));
                    //LOGGER.info(failure.getCause());
                    LOGGER.info(failure.getMessage());
					/*
					 * String causeStr = failure.getMessage();
					 * if(causeStr.contains("document already exists")) {
					 * continue; }
					 */
                    if(failure.getStatus() == RestStatus.BAD_REQUEST) {
                        LOGGER.info("id=" + bulkItemResponse.getId() + "为非法的请求!");
                        continue;
                    }
                }
 
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();
 
                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                    if(bulkItemResponse.getFailure() != null && bulkItemResponse.getFailure().getStatus() == RestStatus.CONFLICT) {
                        LOGGER.info("id=" + bulkItemResponse.getId() + "与现在文档冲突");
                        continue;
                    }
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    LOGGER.info("id=" + indexResponse.getId() + "的文档创建成功");
                    LOGGER.info("id=" + indexResponse.getId() + "文档操作类型：" + itemResponse.getResult());
                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    LOGGER.info("id=" + updateResponse.getId() + "的文档更新成功");
                    LOGGER.info("id=" + updateResponse.getId() +"文档内容为：" + updateResponse.getGetResult().sourceAsString());
                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                        LOGGER.info("id=" + deleteResponse.getId() + "的文档未找到，未执行删除!");
                    }else {
                        LOGGER.info("id=" + deleteResponse.getId() + "的文档删除成功");
                    }
                }
            }
			LOGGER.info("同步方法同步成功:"+String.valueOf(id));
			//异步方式
	    	/**
			client.bulkAsync(request, new ActionListener<BulkResponse>() {
			    @Override
			    public void onResponse(BulkResponse bulkResponse) {
			        //成功
			    	LOGGER.info("异步同步成功:"+String.valueOf(id));
			    	
			    }

			    @Override
			    public void onFailure(Exception e) {
			        //失败
			    	LOGGER.info("异步同步失败！"+String.valueOf(id));
			    	//addDataBatch(index,type,id,list);
			    }
			});
			**/
		} catch (IOException e1) {
			e1.printStackTrace();
			flag = false;
		}
        return flag;
    }
 

	/**
	 * @desc检查索引
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public static boolean checkIndexExist(final String index) {
		try {
			Response response = client.getLowLevelClient().performRequest("HEAD", index);
			boolean exist = response.getStatusLine().getReasonPhrase().equals("OK");
			return exist;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @desc根据条件查询
	 * @return
	 */
	public boolean queryByMatch(final String indexName, final String esType, final String field, final String value) {
		try {
			String endPoint = "/" + indexName + "/" + esType + "/_search";

			IndexRequest indexRequest = new IndexRequest();
			XContentBuilder builder;
			try {
				builder = JsonXContent.contentBuilder().startObject().startObject("query")
						.startObject("match").field(field, value).endObject().endObject()
						.endObject();
				indexRequest.source(builder);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String source = indexRequest.source().utf8ToString();

			LOGGER.info("source---->" + source);

			HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);

			Response response = client.getLowLevelClient().performRequest("POST", endPoint,
					Collections.<String, String> emptyMap(), entity);
			LOGGER.info("query data  exist :" + EntityUtils.toString(response.getEntity()));
			// return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("query data in Mapping " + esType + " failed !");
			LOGGER.info("the excepthion: \n " + e.getMessage());
			return false;
		}
		LOGGER.info("query data  exist !");
		return true;
	}

	/**
	 * @desc根据条件查询
	 * @return
	 */
	public boolean query(final String indexName, final String esType, final String field, final String value) {
		boolean isExist = false;
		try {
			String endPoint = "/" + indexName + "/" + esType + "/_search";

			IndexRequest indexRequest = new IndexRequest();
			XContentBuilder builder;
			try {
				builder = JsonXContent.contentBuilder().startObject().startObject("query")
						.startObject("term")
						// name中包含deleteText
						.field(field, value)
						// .field("name.keyword", value)
						.endObject().endObject().endObject();
				indexRequest.source(builder);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String source = indexRequest.source().utf8ToString();

			HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);

			Response response = client.getLowLevelClient().performRequest("POST", endPoint,
					Collections.<String, String> emptyMap(), entity);
			JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
			JSONObject result = (JSONObject) jsonObject.get("hits");
			int total = result.getInteger("total");
//			JSONObject jsonData = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
//			JSONObject result = (JSONObject) jsonData.get("hits");
//			int total = result.getInt(key)
			// FIXME, what it gets
//			int total = 0;
			if (total > 0) {
				isExist = true;
			} else {
				isExist = false;
			}
			LOGGER.info("query data  result :" + EntityUtils.toString(response.getEntity()));
			// return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("query data in Mapping " + esType + " failed !");
			LOGGER.info("the excepthion: \n " + e.getMessage());
			return false;
		}
		return isExist;
	}
	
	/**
	 * 	批量查询
	 * 
	 * @param indexName
	 * @param esType
	 * @param ids
	 * @return
	 */
	public static List<Integer> queryBatch(final String indexName, final String esType, List<String> ids) {
		List<Integer> indexList = new ArrayList<>();
//		Settings settings = Settings.settingsBuilder().put("cluster.name", "es_cluster").build();
//        client = TransportClient.builder().settings(settings).build()
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ES_HOST), ES_TCP_PORT));

		try  {
            // 创建查询父对象
            MultiGetRequest request = new MultiGetRequest();
            // 添加子查询
            for(int i=0;i<ids.size();i++) {
            	request.add(new MultiGetRequest.Item(indexName, esType, ids.get(i)).fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));
            }
            MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
            int count = 0;
            for(MultiGetItemResponse item: response.getResponses()) {
                String index = item.getIndex();
                String type = item.getType();
                String id = item.getId();
                LOGGER.info("第" + ++count + "条-》index:" + index + "; type:" + type + "; id:" + id);
                if(item.getFailure() != null) {
                    Exception e = item.getFailure().getFailure();
                    ElasticsearchException ee = (ElasticsearchException) e;
                    if(ee.getMessage().contains("reason=no such index")) {
                        LOGGER.info("查询的文档库不存在！");
                    }
                }
 
                GetResponse getResponse = item.getResponse();
 
                if (getResponse.isExists()) {
                    long version = getResponse.getVersion();
                    String sourceAsString = getResponse.getSourceAsString();
                    LOGGER.info("查询的结果为：存在");
                    //LOGGER.info(sourceAsString);
                    Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                    byte[] sourceAsBytes = getResponse.getSourceAsBytes();
                } else {
                	LOGGER.info("查询的结果为：不存在");
                	indexList.add(count-1);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
		return indexList;
	}
	
	/**
	 * 	根据类型创建ES索引
	 * 
	 * @param indexName
	 * @param esType
	 * @return
	 */
	private static boolean createMaping(final String indexName, final String esType) {
		try {
			PutMappingRequest request = new PutMappingRequest(indexName);
			if ("ETCPassList".equals(esType)) {
				request.type(esType);
				request.source("{\n" + "  \"properties\": {\n" + "    \"RecordNo\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"SerialNo\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"FlagNetRoadID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"FlagRoadID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"FlagID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"OBUType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"OBUMacID\": {\n"
						+ "      \"type\": \"integer\"\n" + "    },\n" + "    \"OBUNum\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"PayCardID\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"PayCardType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"ICClssuer\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"CPUCardID\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"VehicleType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"VehPlate\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"VehColor\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"OpTime\": {\n"
						+ "      \"type\": \"date\",\n"
						+ "       \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n"
						+ "    },\n" + "    \"Direction\": {\n" + "      \"type\": \"short\"\n"
						+ "    },\n" + "    \"VehStatus\": {\n" + "      \"type\": \"short\"\n"
						+ "    },\n" + "    \"Spare1\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"Spare2\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"Spare3\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"Spare4\": {\n" + "      \"type\": \"integer\"\n"
						+ "    }\n" + "  }\n" + "}", XContentType.JSON);
			}
			if ("etcTradeList".equals(esType)) {
				request.type(esType);
				request.source("{\n" + "  \"properties\": {\n" + "    \"RrecordNo\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"serialNo\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"flagNetRoadID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"flagRoadID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"flagID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"obuType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"obuMacID\": {\n"
						+ "      \"type\": \"integer\"\n" + "    },\n" + "    \"obuNum\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"payCardID\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"payCardType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"payCardIssuer\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"cpuCardID\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"etcTermCode\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"etcTermTradeNo\": {\n"
						+ "      \"type\": \"integer\"\n" + "    },\n" + "    \"transacType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"algFlag\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"tacCode\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"vehicleType\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"vehPlate\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"vehColor\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"opTime\": {\n"
						+ "      \"type\": \"date\",\n"
						+ "       \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n"
						+ "    },\n" + "    \"direction\": {\n" + "      \"type\": \"short\"\n"
						+ "    },\n" + "    \"payCardBabkabceBefore\": {\n"
						+ "      \"type\": \"integer\"\n" + "    },\n"
						+ "    \"payCardBabkabceAfter\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"tollAmount\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"dealStatu\": {\n" + "      \"type\": \"short\"\n"
						+ "    },\n" + "    \"verifyCode\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"spare1\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"spare2\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"spare3\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"spare4\": {\n" + "      \"type\": \"integer\"\n"
						+ "    }\n" + "  }\n" + "}", XContentType.JSON);
			}
			if ("imageList".equals(esType)) {
				request.type(esType);
				request.source("{\n" + "  \"properties\": {\n" + "    \"listNo\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"serialNo\": {\n"
						+ "      \"type\": \"text\"\n" + "    },\n" + "    \"flagNetRoadID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"flagRoadID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"flagID\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"deviceID\": {\n"
						+ "      \"type\": \"integer\"\n" + "    },\n" + "    \"laneNo\": {\n"
						+ "      \"type\": \"short\"\n" + "    },\n" + "    \"OpTime\": {\n"
						+ "      \"type\": \"date\",\n"
						+ "       \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n"
						+ "    },\n" + "    \"directionNo\": {\n" + "      \"type\": \"short\"\n"
						+ "    },\n" + "    \"vehPlate\": {\n" + "      \"type\": \"text\"\n"
						+ "    },\n" + "    \"vehColor\": {\n" + "      \"type\": \"text\"\n"
						+ "    },\n" + "    \"vehSpeed\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"vehBodyColorNo\": {\n"
						+ "      \"type\": \"integer\"\n" + "    },\n"
						+ "    \"vehBodyDeepNo\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"vehTypeNo\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"plateTypeNo\": {\n" + "      \"type\": \"integer\"\n"
						+ "    },\n" + "    \"image\": {\n" + "      \"type\": \"text\"\n"
						+ "    }\n" + "  }\n" + "}", XContentType.JSON);
			}
			PutMappingResponse putMappingResponse = client.indices().putMapping(request,
					RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("create Mapping " + esType + " failed !");
			LOGGER.info("the excepthion: \n " + e.getMessage());
			return false;
		}
		LOGGER.info("create Mapping " + esType + " success !");
		return true;
	}
	
	/**
	 * 	检查索引是否存在
	 * 
	 * @param topic
	 * @param key
	 * @param data
	 * @return
	 */
	public <ID> boolean checkDataExist(final String topic, final String key,
			final TrafficData<ID> data) {
		// 将topic作为es的index
		String indexName = topic.replace("-", "");
		// String indexName = "po_01";
		if (!checkIndexExist(indexName)) {
			if (createIndex(indexName)) {
				LOGGER.info("创建索引成功");
			} else {
				LOGGER.info("创建索引失败");
			}
		}
		return false;
	}

	/**
	 * @desc 关闭连接
	 */
	public void close() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 	获取es客户端
	 * 
	 * @return
	 */
	public static RestHighLevelClient getClient() {
		return client;
	}

}
