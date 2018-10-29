package com.wbd.portal.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wbd.pojo.TbContent;
import com.wbd.portal.service.ContentService;

@Controller
public class IndexController {
	@Value("${CONTENT_LUNBO_CID}")
	private long CONTENT_LUNBO_CID;
	
	@Autowired
	private  ContentService  contentService;
	
	/**
	 * 显示首页
	 * <p>Title: showIndex</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/index")
	public String showIndex(Model model) {
		
		List<TbContent> ad1List = contentService.getContentListByCategoryId(CONTENT_LUNBO_CID);
		
		model.addAttribute("ad1List", ad1List);
		return "index";
	}

}
