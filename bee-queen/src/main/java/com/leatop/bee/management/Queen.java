/**
 * File: QueenApp.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月24日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * Main entry point for this application.
 * 
 * @author Dorsey
 *
 */
@EnableAdminServer
@SpringBootApplication
@MapperScan(basePackages = { "com.leatop.bee.management.dao" })
public class Queen {

	public static void main(final String[] args) {
		SpringApplication.run(Queen.class, args);
	}
	
	@Controller
	public static class IndexController {

		@ResponseBody
		@GetMapping("/home")
		public ModelAndView index(Model model) {
			ModelAndView mv = new ModelAndView("index");
	        return mv;
		}
	}
}
