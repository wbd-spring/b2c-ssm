package com.wbd.manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	/**
	 * 首页
	 * <p>Title: showIndex</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}

	/**
	 * 根据请求路径跳转到对应的jsp页面
	 * <p>Title: showPage</p>  
	 * <p>Description: </p>  
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
	
	
}
