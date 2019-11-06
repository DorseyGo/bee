/**
 * File: RecordCounterReporter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月12日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.reporter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.domain.RecordCounter;
import com.leatop.bee.web.resp.Resp;

/**
 * 	消息记录统计
 * 
 * @author Dorsey
 *
 */
@Component
public class RecordCounterReporter {

	private static final Logger LOG = LoggerFactory.getLogger(RecordCounterReporter.class);
	private final RestTemplate template;
	private final RecordCounter recordCounter;
	private final BeeWebConfiguration config;

	@Autowired
	public RecordCounterReporter(final RestTemplate template, final BeeWebConfiguration config) {
		this.template = template;
		this.recordCounter = RecordCounter.getInstance();
		this.config = config;
	}

//	@Scheduled(cron = "0/3 * * * * ?")
	public void reportInHours() {
		final String uri = "/datatrans/saveToCache?topicName={1}&transCount={2}";
		final String requestUrl = buildRequestUrl(uri);
		final Map<String, AtomicLong> alls = recordCounter.unmodifiableAlls();

		Resp resp = null;
		for (Map.Entry<String, AtomicLong> entry : alls.entrySet()) {
			resp = template.getForObject(requestUrl, Resp.class, entry.getKey(),
					entry.getValue().get());

			if (resp.getStatusCode() == Resp.FAILURE.getStatusCode()) {
				LOG.warn("Failed to report transfer records: #{} to topic: {} every 3 secs",
						entry.getValue().get(), entry.getKey());
			}
		}
		
		// clean
		recordCounter.clear();
	}

	private String buildRequestUrl(final String uri) {
		return config.getReporterUrl() + "/" + uri;
	}

//	@Scheduled(cron = "0 0 */1 * * ?")
//	@Scheduled(cron="0 */1 * * * ?")
	@Scheduled(cron = "0/3 * * * * ?")
	public void reportInSeconds() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();  
		final String hostName=addr.getHostName().toString(); //获取本机计算机名称  
		final String uri = "/datatrans/saveToKafka?hostName="+hostName+"&topicName={1}&successCount={2}&errorCount={3}";
		final String requestUrl = buildRequestUrl(uri);
		synchronized(recordCounter.lock) {
			Map<String, AtomicLong> success = recordCounter.unmodifiableSuccess();
			Map<String, AtomicLong> failed = recordCounter.unmodifiableFails();
	
			Resp resp = null;
			AtomicLong failCount = null;
			if (success.size() == failed.size()) {
				for (Map.Entry<String, AtomicLong> entry : success.entrySet()) {
					//有数据传输时才调接口
					if (entry.getValue().get()>0) {
						resp = template.getForObject(requestUrl, Resp.class, entry.getKey(),
								entry.getValue().get(),
								((failCount = failed.get(entry.getKey()))  == null ? 0 : failCount.get() ));
						if (resp.getStatusCode() == Resp.FAILURE.getStatusCode()) {
							LOG.warn("Failed to report transfer records: success:#{}, failure:#{} to topic: {} every 1 hour",
									entry.getValue().get(), failCount.get(), entry.getKey());
						}
						//success.put(entry.getKey(), new AtomicLong(0));
					}
				}
				//清零
				recordCounter.clear();
				return;
			}
			
			// complements
			if (success.size() > failed.size()) {
				for (Map.Entry<String, AtomicLong> entry : success.entrySet()) {
					//有数据传输时才调接口
					if (entry.getValue().get()>0) {
						resp = template.getForObject(requestUrl, Resp.class, entry.getKey(),
								entry.getValue().get(), 0L);
						
						if (resp.getStatusCode() == Resp.FAILURE.getStatusCode()) {
							LOG.warn("Failed to report transfer records: success:#{}, failure:#{} to topic: {} every 1 hour",
									entry.getValue().get(), 0L, entry.getKey());
						}
						//success.put(entry.getKey(), new AtomicLong(0));
					}
				}
				//清零
				recordCounter.clear();
				return;
			}
			
			if (success.size() < failed.size()) {
				for (Map.Entry<String, AtomicLong> entry : failed.entrySet()) {
					//有数据传输时才调接口
					if (entry.getValue().get()>0) {
						resp = template.getForObject(requestUrl, Resp.class, entry.getKey(),
								0L, entry.getValue().get());
						
						if (resp.getStatusCode() == Resp.FAILURE.getStatusCode()) {
							LOG.warn("Failed to report transfer records: success:#{}, failure:#{} to topic: {} every 1 hour",
									0L, entry.getValue().get(), entry.getKey());
						}
						//failed.put(entry.getKey(), new AtomicLong(0));
					}
				}
				//清零
				recordCounter.clear();
				return;
			}
		}
	}
}
