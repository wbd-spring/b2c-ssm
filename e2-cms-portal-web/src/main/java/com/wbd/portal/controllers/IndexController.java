package com.wbd.portal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	/**
	 * 显示首页
	 * <p>Title: showIndex</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/index")
	public String showIndex() {
		
		return "index";
	}

}
