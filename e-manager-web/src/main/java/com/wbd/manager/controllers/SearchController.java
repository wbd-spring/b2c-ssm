package com.wbd.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.utils.WBDResult;
import com.wbd.search.service.SearchService;

/**
 * 搜索服务控制器
* <p>Title: SearchController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月29日
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	/**
	 * 导入数据库的数据作为solr的索引库
	 * <p>Title: importSolrIndex</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public WBDResult importSolrIndex() {
		
		return searchService.importAllItemInfo();
		
	}
}
