package com.wbd.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbd.common.pojo.SearchResult;
import com.wbd.search.service.SearchService;
import com.wbd.search.service.dao.SearchDAO;
/**
 * 系统的搜索服务
* <p>Title: SearchServiceImpl.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月30日
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDAO  searchDAO;
	
	
	
	@Override
	public SearchResult search(String keyword, Integer page, Integer rows) {
		
		SolrQuery sq = new SolrQuery();
		//1.设置查询内容
		sq.setQuery(keyword);
		//2.设置默认查询域
		sq.set("df", "item_keywords");
		
		//3.设置分页查询条件
		sq.setStart((page-1)*rows);
		sq.setRows(rows);
		
		//4.高亮设置
		sq.setHighlight(true);  //开启高亮
		sq.addHighlightField("item_title"); //设置标题为高亮显示字段
		sq.setHighlightSimplePre("<em>");
		sq.setHighlightSimplePost("</em>");
		
		SearchResult search = searchDAO.search(sq);
		
		//5.计算总页数
		long recourdCount = search.getRecourdCount(); //总记录数
		long totalPages = recourdCount/rows;
		if(totalPages % rows >0) totalPages++;
		search.setTotalPages(totalPages);
		return search;
	}

}
