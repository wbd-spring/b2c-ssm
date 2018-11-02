package com.wbd.search.service.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.wbd.search.service.impl.ItemIndexImportServiceImpl;
/**
 * 把商品添加到solr索引库，在监听器开启的监听时服务层的addDocument方法
 */
public class ItemChangeListener implements MessageListener {

	@Autowired
	private ItemIndexImportServiceImpl  itemIndexImportServiceImpl;
	
	@Override
	public void onMessage(Message message) {

		TextMessage tm = (TextMessage) message;
		try {
		long itemId=	Long.parseLong(tm.getText());
		
		//等待事务提交
		try {
			Thread.sleep(1000);
			itemIndexImportServiceImpl.addDocument(itemId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
