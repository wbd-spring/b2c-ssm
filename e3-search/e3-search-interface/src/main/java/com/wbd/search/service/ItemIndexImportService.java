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
	
	/**
	 * 导入所以商品信息到索引库
	 * <p>Title: importAllItemInfo</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	public WBDResult importAllItemInfo();
	
	/**
	 * 当添加完商品之后， 通过mq 把商品id发送到mq服务器，
	 * mq服务器把id推给消费者， 消费者监听消息获取 id，通过id
	 *获取商品信息，然后把该商品信息写入solr库。
	 * <p>Title: addDocument</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @return
	 */
	public WBDResult addDocument(long itemId);

}
