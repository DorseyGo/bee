/**
 * File: Application.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月22日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Main entry point for the entire web project.
 * 
 * @author Dorsey
 *
 */
@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Controller
	public static class IndexController {

		@ResponseBody
		@GetMapping("/index")
		public String index() {
			return "hello, Queen of bees";
		}
	}
	
	@Bean
	public RestTemplate restTemplate(final ClientHttpRequestFactory factory) {
		return new RestTemplate(factory);
	}
	
	@Bean
	public ClientHttpRequestFactory simpleHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(3000);
		factory.setConnectTimeout(5000);
		
		return factory;
	}
}
