package com.wbd.search.service;

import com.wbd.common.pojo.SearchResult;

/**
 * 首页的搜索服务
 * 入参：
 * 1.page
 * 2.keyword
 * 3.rows=pagesize
 * 返回值：
 * 
* <p>Title: SearchService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月30日
 */
public interface SearchService {
	
	/**
	 * 
	 * <p>Title: search</p>  
	 * <p>Description: </p>  
	 * @param keyword 关键字，即搜索的内容
	 * @param page 页码，默认为1
	 * @param rows 每页显示的条数，可设置
	 * @return
	 */
	public SearchResult search(String keyword,Integer page, Integer rows);

}
