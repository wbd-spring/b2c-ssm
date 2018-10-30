package com.wbd.search.service;
/**
 *  商品索引库导入solr服务器接口
* <p>Title: SearchService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月29日
 */

import com.wbd.common.utils.WBDResult;

public interface ItemIndexImportService {
	
	public WBDResult importAllItemInfo();

}
