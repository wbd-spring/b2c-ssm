package com.wbd.search.service;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
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
	
	
}
