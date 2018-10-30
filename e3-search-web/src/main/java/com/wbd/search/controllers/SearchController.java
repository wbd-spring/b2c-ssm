package com.wbd.search.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wbd.common.pojo.SearchResult;
import com.wbd.search.service.SearchService;

/**
 * 搜索服务控制器
* <p>Title: SearchController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月30日
 */
@Controller
public class SearchController {
	
	
	@Autowired
	private SearchService  searchService;
	
	@Value("${PAGE_SIZE}")
	private Integer PAGE_SIZE;
	
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1") Integer page,Model model) {
		
		//1.将keyword进行转码
		
		try {
			keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
			
			SearchResult search = searchService.search(keyword, page, PAGE_SIZE);
			
			model.addAttribute("query",keyword);
			model.addAttribute("totalPages",search.getTotalPages());
			model.addAttribute("page",page);
			model.addAttribute("recourdCount",search.getRecourdCount());
			model.addAttribute("itemList",search.getItemList());
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "search";
	}
	

}
