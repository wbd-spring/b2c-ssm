package com.wbd.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.utils.WBDResult;
import com.wbd.search.service.ItemIndexImportService;

/**
 * 商品索引库导入solr服务器控制器
* <p>Title: SearchController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月29日
 */
@Controller
public class ItemIndexImportControll {

	@Autowired
	private ItemIndexImportService itemIndexImportService;
	
	/**
	 * 导入数据库的数据作为solr的索引库
	 * <p>Title: importSolrIndex</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public WBDResult importSolrIndex() {
		
		return itemIndexImportService.importAllItemInfo();
		
	}
}
