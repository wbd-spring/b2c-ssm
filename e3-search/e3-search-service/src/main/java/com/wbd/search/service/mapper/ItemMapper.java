package com.wbd.search.service.mapper;

import java.util.List;

import com.wbd.common.pojo.SearchItem;

/**
 * 自定义 mybatis mapper接口
* <p>Title: ItemMapper.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月29日
 */
public interface ItemMapper {

	/**
	 * 查询数据库中 商品所关联商品类别的信息，放入SearchItem对象中， 
	 * 目的为把SearchItem对象存入solr库中。
	 * <p>Title: getItemList</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	List<SearchItem> getItemList();
	
	/**
	 * 根据商品id查询出 tbitem和tbitemdesc两个表的信息
	 * <p>Title: getItemById</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @return
	 */
	SearchItem getItemById(long itemId);
	
}
