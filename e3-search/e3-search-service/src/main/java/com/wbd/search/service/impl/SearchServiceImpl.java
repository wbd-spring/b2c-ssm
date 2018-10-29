package com.wbd.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbd.common.pojo.SearchItem;
import com.wbd.common.utils.WBDResult;
import com.wbd.search.service.SearchService;
import com.wbd.search.service.mapper.ItemMapper;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	//注入(在spring文件中)一个SolrServer的子类， 因为SolrServer是抽象类
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public WBDResult importAllItemInfo() {
		try {
			//1.查询商品信息
			
			List<SearchItem> itemList = itemMapper.getItemList();
			
			for (SearchItem searchItem : itemList) {
			
			// 创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				
			// 将文档添加到 solrserver
				solrServer.add(document);
			}
			
			// solrserver提交
			
			solrServer.commit();
			
			//2.把信息迭代添加到solr索引库中
			return WBDResult.ok();
		}  catch (IOException | SolrServerException e) {
			e.printStackTrace();
			return WBDResult.build(500, "solr导入数据出现异常");
		}
	}

}
