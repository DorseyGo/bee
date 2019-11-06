/**
 * File: ApplicationTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月22日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.leatop.bee.common.log.LogFacade;
import com.leatop.bee.common.utils.ElasticSearchUtils;



/**
 * A demon for unit test via Spring framework.
 * 
 * @author Dorsey
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class ApplicationTest {
	
	private LogFacade logfacade = LogFacade.getFacade(ApplicationTest.class);
	
	//@Test
	public void test() {
		Assert.assertTrue(true);
	}
	//@Test
	public void testESCreateIndex() {
		String indexName = "test_index0505";
		if(!ElasticSearchUtils.getInstance().checkIndexExist(indexName)) {
			boolean flag = ElasticSearchUtils.getInstance().createIndex(indexName);
			if(flag) {
				logfacade.info("create index "+indexName+" success!");
			}else {
				logfacade.info("create index "+indexName+" failed !");
			}
		}else {
			logfacade.info("create index  "+indexName+" exit !");
		}
	}
	@Test
	public void testESAddData() {
		String indexName = "test_index0505";
		String esType = "test_index";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        JSONObject jsonObject = new JSONObject();
        try {
			jsonObject.put("id", sdf.format(new Date()));
			jsonObject.put("age", 29);
	        jsonObject.put("name", "liming");
	        jsonObject.put("date", new Date());
	        String id=ElasticSearchUtils.getInstance().addData(indexName, esType, jsonObject.getString("id"), jsonObject);
	        if(!id.isEmpty()){
	        	logfacade.info("add data to  "+indexName+" success !");
	        }
	        else{
	        	logfacade.info("add data to  "+indexName+" failed !");
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
       
	}
}
