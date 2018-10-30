package com.wbd.search.service.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wbd.common.pojo.SearchItem;
import com.wbd.common.pojo.SearchResult;

/**
 * 查询Solr 库的操作
* <p>Title: SearchDAO.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月30日
* 
* @Repository 注入spring容器，让其管理
 */
@Repository
public class SearchDAO {
	
	//注入 SolrServer,已经再spring配置中注入了SolrServer
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery query) {
		
		SearchResult sr = new SearchResult();
		try {
		QueryResponse  queryResponse=	solrServer.query(query);
		//1.获得总记录数
		SolrDocumentList results = queryResponse.getResults();
		//2.设置总记录数
		sr.setRecourdCount(results.getNumFound());
		//3.创建 SearchItem list
		List<SearchItem> list = new ArrayList<SearchItem>();
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : results) {
			
		 // 4.创建 SearchItem对象
			SearchItem si = new SearchItem();
			si.setId((String) solrDocument.get("id"));
		
		 //5.获取高亮中的标题	
			List<String> list2 = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
            if(list2!=null && list2.size()>0) {
            	
            	title = list2.get(0);
            }else {
            	
            	title = (String) solrDocument.get("item_title");
            }	
            si.setTitle(title);
            si.setPrice((long) solrDocument.get("item_price"));
            si.setSell_point((String) solrDocument.get("item_sell_point"));
            si.setImage((String) solrDocument.get("item_image"));
            si.setCategory_name((String) solrDocument.get("item_category_name"));
            list.add(si);
		}
		
		//6.设置itemList
		sr.setItemList(list);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sr;
	}
	
	

}
