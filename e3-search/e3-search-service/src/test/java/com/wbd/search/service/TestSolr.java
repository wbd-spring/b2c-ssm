package com.wbd.search.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * 测试远程连接服务器solr，
* <p>Title: TestSolr.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月29日
 */
public class TestSolr {

	
	/**
	 * 删除
	 * <p>Title: deleteDocument</p>  
	 * <p>Description: </p>
	 */
	@Test
	public void deleteDocument() {
	//1.创建SolrServer 对象， SolrServer为抽象类，只能用他的子类或者实现类
		
		SolrServer solrServer = new HttpSolrServer("http://118.190.71.215:8090/solr/mycore");
		
		try {
			solrServer.deleteById("solr001");
			solrServer.commit();
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 添加
	 * <p>Title: testSolrAddDocument</p>  
	 * <p>Description: </p>
	 */
	@Test
	public void testSolrAddDocument() {
		
		//1.创建SolrServer 对象， SolrServer为抽象类，只能用他的子类或者实现类
		
		SolrServer solrServer = new HttpSolrServer("http://118.190.71.215:8090/solr/mycore");
		
		//2.创建SolrInputDument 
		
		SolrInputDocument document = new SolrInputDocument();
		
		//3.向文档对象中添加域,域中必须包含一个id属性
		
		document.addField("id", "solr001");
		document.addField("item_title", "共享经济");
		document.addField("item_price", 5000);
		
		//4.向solrServer添加文档
		try {
			solrServer.add(document);
		//5.提交
			solrServer.commit();
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 查询
	 * 
	 * 1.创建solserver对象
	 * 2.创建query对象
	 * 3.设置query对象
	 * 4.执行查询返回queryresponse对象
	 * 5.获取结果
	 * 6.循环遍历结果
	 * <p>Title: query</p>  
	 * <p>Description: </p>
	 */
	@Test
	public void query() {
		
		//1.创建solerserver
		SolrServer solrServer = new HttpSolrServer("http://118.190.71.215:8090/solr/mycore");
		
		//2.创建solrquery对象
		SolrQuery solrQuery = new SolrQuery();
		
		//3.设置query查询条件 *:*表示查询所有
		
		solrQuery.setQuery("*:*"); 
		
		//4.将solrquery放入solrserver中返回QueryResponse对象
		
		try {
		QueryResponse queryResponose = 	solrServer.query(solrQuery);
		
		//5.获取返回结果对象
		SolrDocumentList dlist = queryResponose.getResults();
		
		//6.获取总记录数
		long numFound = dlist.getNumFound();
		
		System.out.println("总记录数"+numFound);
		//7.迭代循环获取其他的的值
		
		for (SolrDocument solrDocument : dlist) {
			
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
		
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 基于条件查询，让标题高亮
	 * <p>Title: queryDocumentWithHighLighting</p>  
	 * <p>Description: </p>
	 */
	@Test
	public void queryDocumentWithHighLighting() {
		
		SolrServer solrSerser = new HttpSolrServer("http://118.190.71.215:8090/solr/mycore");
		SolrQuery solrQuery = new SolrQuery();
		//设置条件
		solrQuery.setQuery("阿尔卡特");
		
		//指定默认搜索域，也是我们在solr服务器配置文件的配置的那些域的其中一个
		solrQuery.set("df", "item_keywords");
		
		//设置分页条件
		solrQuery.setStart(1);
		solrQuery.setRows(2);
		//开启高亮显示
		solrQuery.setHighlight(true);
		
		//高亮显示的域
		solrQuery.addHighlightField("item_title");
	
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		
		try {
			QueryResponse query = solrSerser.query(solrQuery);
			
			SolrDocumentList results = query.getResults();
			
			//获取总记录数
			
			long numFound = results.getNumFound();
			
			System.out.println("总记录数="+numFound);

			
			Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
			
			
			
			for (SolrDocument solrDocument : results) {
				
				System.out.println("id="+solrDocument.get("id"));
				
				//取高亮显示
				List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
				
				String title = "";
				if(list!=null && list.size()>0) {
					
					title = list.get(0); //高亮显示的list中一般只有一个值
				}else {
					
					title =(String) solrDocument.get("item_title");
				}
				
				System.out.println("标题="+title);
				System.out.println("卖点="+solrDocument.get("item_sell_point"));
				System.out.println("价格="+solrDocument.get("item_price"));
			}
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
