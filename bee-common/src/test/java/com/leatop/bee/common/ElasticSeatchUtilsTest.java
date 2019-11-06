/**
 * File: DataWeaveInManagerTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
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
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.LoggerFactory;

import com.leatop.bee.common.utils.ElasticSearchUtils;

import ch.qos.logback.classic.Logger;

/**
 * @author Dorsey
 *
 */

public class ElasticSeatchUtilsTest {

	private static final Logger LOGGER = (Logger) LoggerFactory
			.getLogger(ElasticSeatchUtilsTest.class);
	
	private final static ObjectMapper mapper = new ObjectMapper();

	private String[] carNo = { "粤G48443", "闽E36618", "闽F86659", "辽PE7927", "闽F92285", "闽DA5722",
			"辽A9393X", "辽PD0750", "闽E38626", "闽E37532", "辽AX5710", "闽E37251", "湘A41666", "闽DA3310",
			"闽E38272", "鄂F2J118", "闽E38858", "湘L35386", "闽E38990", "闽E38587", "闽B26855", "湘L6B882",
			"湘H23399", "闽B26819", "鄂L75169", "闽E38999", "闽E37231", "闽E38361", "闽F92183", "湘J55288",
			"湘F37509", "湘DA7357", "湘A8B395", "湘J56878", "湘L4E940", "粤ZAD30澳", "粤ZDX07港", "粤ZBW08港",
			"粤ZFE78港", "粤ZGF41港", "粤ZDZ99港", "粤ZBB38港", "粤ZGT25港", "粤ZFS94港", "粤ZHD04港", "粤ZDZ03港",
			"粤ZYN88港", "粤ZHJ33港", "粤ZYK65港", "粤ZCK36港", "粤ZAR84港", "粤ZHU77港", "粤ZDG42港", "粤ZFS21港",
			"粤G54960", "粤UX1436", "粤H18585", "粤GJ3922", "粤KS5917", "粤KS6101", "粤KN1444", "粤K23550",
			"粤G46972", "粤KK8426", "粤CJ5152", "粤GR7746", "粤KP9050", "粤BDJ267", "粤ZHS84港", "粤KP9119",
			"粤GE0450", "粤KS4511", "粤KP8248", "粤K8M002", "粤BBM826", "粤G56197", "粤KS5041", "粤GM9229",
			"粤BP6309", "粤G56185", "粤G88888", "粤HG6938", "粤G56907", "粤KK7820", "粤KS4096", "粤BT2582",
			"粤KK6035", "粤SF1873", "粤AV9711", "粤ZB054港", "粤GJ4039", "粤BBE219", "粤KS6093", "粤KS4758",
			"粤GM9017", "粤C27615", "粤KP9045", "粤K17540", "粤GE0675", "粤BBY837", "粤BCH467", "粤AS8187",
			"粤GE0687", "粤QV1713", "粤GM3557", "粤K20056", "粤BBX363", "粤P3C017", "粤KP8417", "粤G51918",
			"粤UX1789", "粤GJ3450", "粤GE0559", "粤GE0031", "粤ZGZ28澳", "粤ZEB56港", "粤GM7369", "粤GE1431",
			"粤P13092", "粤GE0969", "粤BBY836", "粤HG4961", "粤GM3597", "粤BCT970", "粤C47760", "粤BCU153",
			"粤GJ4262", "粤A59853", "粤GM6238", "粤MK3216", "粤GR9773", "粤K29987", "粤BCG296", "粤AV9570",
			"粤GJ3832", "粤CL6199", "粤BW0601", "粤KK7115", "粤BDP357", "粤E38858", "粤BH0908", "粤BCU627",
			"粤L66263", "粤G56321", "粤H21348", "粤GE1192", "粤HK3847", "粤J02454", "粤GM8399", "粤BAV415",
			"粤GE9448", "粤SE6285", "粤F67656", "粤BK1255", "粤BCX127", "粤GU7328", "粤GM8825", "粤GM7366",
			"粤BCB932", "粤ZFB78港", "粤GM7809", "粤GJ4373", "粤GR9250", "粤GE0533", "粤GM8327", "粤BAP571",
			"粤MK5541", "粤KS4535", "粤AE2501", "粤P21728", "粤BQ1722", "粤DA8976", "粤AE2268", "粤BDC213",
			"粤KK7259", "粤GE1156", "粤AAN512", "粤BDP355", "粤BAV377", "粤X31779", "粤XWM896", "粤BM7571",
			"粤BDV028", "粤K65229", "粤K65063", "粤C7A627", "粤AE3112", "粤GJ3223", "粤GQ1316", "粤BX4371",
			"粤K18317", "粤C29146", "粤P21627", "粤D87461", "粤BX8690", "粤CR8782", "粤C23274", "粤G56949",
			"粤GM3693", "粤K25798", "粤BBZ172", "粤GM9993", "粤P21701", "粤CT6385", "粤AE2325", "粤BBZ148",
			"粤BDC633", "粤AV9572", "粤M23203", "粤BAZ293", "粤BCR261", "粤BAZ325", "粤GR9706", "粤BAB959",
			"粤AV7E25", "粤AV3407", "粤GJ3956", "粤CT1821", "粤ZDJ83港", "粤BER391", "粤BS3522", "粤GE1370",
			"粤G56157", "粤E31208", "粤M29358", "粤GM5831", "粤BCU239", "粤RK2890", "粤BZ7853", "粤BAK262",
			"粤GE1753", "粤BAZ276", "粤G57721", "粤BAL129", "粤AV9575", "粤GE0056", "粤L47889", "粤BW7473",
			"粤KP9328", "粤BCT415", "粤BDE399", "粤RD9777", "粤BBE113", "粤BBZ192", "粤C93192", "粤SE4051",
			"粤LB5277", "粤K66289", "粤CA8818", "粤BBY847", "粤GR9935", "粤CX0947", "粤G56300", "粤BDY690",
			"粤BBM529", "粤GE0660", "粤CN6272", "粤BU6284", "粤GE0062", "粤BW8523", "粤QV6768", "粤BY3588",
			"粤CX6431", "粤BCX178", "粤BBU679", "粤BK8370", "粤K52633", "粤G56550", "粤BBR538", "粤BN8585",
			"粤K29833", "粤L48502", "粤GM5962", "粤BBJ251", "粤BDP295", "粤C49955", "粤BAR628", "粤BDM198",
			"粤BEH145", "粤FA5925", "粤D11261", "粤BV2977", "粤BT4408", "粤D8A222", "粤BBZ202", "粤PF5933",
			"粤T3436W", "粤G57995", "粤BCU665", "粤GM7398", "粤GM5731", "粤BBD755", "粤S2Q675", "粤BDD578",
			"粤U71168", "粤GE0403", "粤HG4931", "粤P21729", "粤CH2743", "粤G56285", "粤BNC558", "粤GU8321",
			"粤BDK955", "粤K2W990", "粤HK4320", "粤K59386", "粤BC1722", "粤BN8225", "粤BBZ031", "粤CN6386",
			"粤T20980", "粤GE0443", "粤K68293", "粤GM8119", "粤CX2618", "粤ALU522", "粤BS7619", "粤K81912",
			"粤BCU238", "粤K65839", "粤BX8209", "粤G76162", "粤BCX293", "粤GE0590", "粤K80399", "湘E60666",
			"湘E60776", "湘E60788", "湘E60929", "湘E60939", "湘E61013", "湘E61035", "湘E61075", "湘E61112",
			"湘E61125", "湘E61136", "湘E61137", "湘E61223", "湘E61228", "湘E61235", "湘E61238", "湘E61239",
			"湘E60785", "湘E60883", "湘E60948" };

	public void checkDataExist() {
		String indexName = "po_01";
		String esType = "ETCPassList";
		String field = "serialNo.keyword";
		// String field = "serialNo";
		String value = "d0650231-90c8-4925-a3a1-8254f7974561";
		// String value ="d0650231-90c8-4925-a3a1-8254f7974563";
		Instant inst1 = Instant.now();
		LOGGER.info("Inst1 : " + inst1);
		boolean isExist = ElasticSearchUtils.getInstance().query(indexName, esType, field, value);
		LOGGER.info("query data isExist: " + isExist);
		Instant inst2 = Instant.now();
		LOGGER.info("Difference in milliseconds : " + Duration.between(inst1, inst2).toMillis());
		LOGGER.info("Difference in seconds : " + Duration.between(inst1, inst2).getSeconds());
	}

	public void createIndex() {
		ElasticSearchUtils.createIndex("po_04");
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
	    		//IndexRequest indexRequest = new IndexRequest(index, type, String.valueOf(id-list.size()+i));
	    		IndexRequest indexRequest = new IndexRequest(index, type, "b30a929b-761d-43c9-9871-5-"+i);
	    		indexRequest.source(mapper.writeValueAsString(list.get(i)), XContentType.JSON);
	    		request.add(indexRequest.source(mapper.writeValueAsString(list.get(i)), XContentType.JSON).opType(DocWriteRequest.OpType.CREATE));
	    	}
	    	request.timeout(TimeValue.timeValueMinutes(2)); 
	    	request.timeout("2m");
	    	
	    	request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); 
	    	request.setRefreshPolicy("wait_for");
	    	//同步方式
			BulkResponse bulkResponse = ElasticSearchUtils.getClient().bulk(request,RequestOptions.DEFAULT);
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
 

	public void queryBatch(final String indexName, final String esType, List<String> ids) {

		// Settings settings = Settings.settingsBuilder().put("cluster.name",
		// "es_cluster").build();
		// client = TransportClient.builder().settings(settings).build()
		// .addTransportAddress(new
		// InetSocketTransportAddress(InetAddress.getByName(ES_HOST),
		// ES_TCP_PORT));
		// RestHighLevelClient client = ElasticSearchUtils.getClient();
		try {
			// 创建查询父对象
			MultiGetRequest request = new MultiGetRequest();
			// 添加子查询
			request.add(new MultiGetRequest.Item(indexName, esType, "7426000")
					.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));
			request.add(new MultiGetRequest.Item(indexName, esType, "7426002")
					.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));
			request.add(new MultiGetRequest.Item(indexName, esType, "sdfsdf")
					.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));
			request.add(new MultiGetRequest.Item(indexName, esType, "b30a929b-761d-43c9-9871-5-9")
					.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));
			MultiGetResponse response = ElasticSearchUtils.getClient().mget(request,
					RequestOptions.DEFAULT);
			int count = 0;
			for (MultiGetItemResponse item : response.getResponses()) {
				String index = item.getIndex();
				String type = item.getType();
				String id = item.getId();
				System.out.println(
						"第" + ++count + "条-》index:" + index + "; type:" + type + "; id:" + id);
				if (item.getFailure() != null) {
					Exception e = item.getFailure().getFailure();
					ElasticsearchException ee = (ElasticsearchException) e;
					if (ee.getMessage().contains("reason=no such index")) {
						System.out.println("查询的文档库不存在！");
					}
				}

				GetResponse getResponse = item.getResponse();

				if (getResponse.isExists()) {
					long version = getResponse.getVersion();
					String sourceAsString = getResponse.getSourceAsString();
					System.out.println("查询的结果为：存在");
					// System.out.println(sourceAsString);
					Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
					byte[] sourceAsBytes = getResponse.getSourceAsBytes();
				} else {
					System.out.println("查询的结果为：不存在");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addDataToES() {
		
		// ElasticsearchUtil.createMaping();
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ETCPassList> list = new ArrayList<ETCPassList>();
		for (int i = 0; i <= 10; i++) {
			// for(int i=1;i<=100;i++) {
			ETCPassList etcPassList = new ETCPassList();
			etcPassList.setRecordNo((short) (random.nextInt(99) + 1));
			etcPassList.setSerialNo(UUID.randomUUID().toString());
			etcPassList.setFlagNetRoadID((short) (random.nextInt(99) + 1));
			etcPassList.setFlagRoadID((short) (random.nextInt(99) + 1));
			etcPassList.setFlagID((short) (random.nextInt(99) + 1));
			etcPassList.setOBUType((short) (random.nextInt(1) + 1));
			etcPassList.setOBUMacID(random.nextInt(9999) + 1);
			etcPassList.setOBUNum(UUID.randomUUID().toString());
			etcPassList.setPayCardID(UUID.randomUUID().toString());
			etcPassList.setPayCardType((short) (random.nextInt(1) + 1));
			etcPassList.setVehPlate(carNo[random.nextInt(345)]);
			etcPassList.setVehColor(String.valueOf(random.nextInt(10)));
			etcPassList.setOpTime(new Date());
			etcPassList.setDirection((short) (random.nextInt(4) + 1));
			// 单个添加
			// ElasticSearchUtils.addData("po_04", "ETCPassList", "dshfkdjs",etcPassList);
			// 批量添加
			list.add(etcPassList);
			if(i%10==0) {
			//if (true) {
				boolean flag = addDataBatch("po_01", "ETCPassList", i, list);
				if (flag) {
					list.clear();
				} else {
					addDataBatch("po_01", "ETCPassList", i,list);
					list.clear();
				}

			}

		}
	}

	public static void main(String[] args) {
		new ElasticSeatchUtilsTest().checkDataExist();
//		Instant inst1 = Instant.now();
//		LOGGER.info("Inst1 : " + inst1);
//		//new ElasticSeatchUtilsTest().addDataToES();
//
//		List<String> list = new ArrayList<String>();
//		list.add("id=b30a929b-761d-43c9-9871-5-0");
//		list.add("id=b30a929b-761d-43c9-9871-5-1");
//		list.add("id=b30a929b-761d-43c9-9871-5-2");
//		new ElasticSeatchUtilsTest().queryBatch("po_01", "ETCPassList", list);
//		Instant inst2 = Instant.now();
//		LOGGER.info("Difference in milliseconds : " + Duration.between(inst1, inst2).toMillis());
//		LOGGER.info("Difference in seconds : " + Duration.between(inst1, inst2).getSeconds());
	}

}
