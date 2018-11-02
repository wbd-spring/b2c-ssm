package com.wbd.search.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
/**
 * 提供者利用jmsTemplate进行发消息
 * 消费者利用MessageListener接口来接收消息。不需要配置jmsTemplate
 * 但是必须要配置MessageListener实现bean
* <p>Title: MyMessageListener.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月1日
 */
public class MyMessageListener implements MessageListener {

	/**
	 * 监听服务器 推送过来的消息， 消息消费者不是去服务器端取数据
	 * 而是服务器把消息推送出来。消费者进行监听即可
	 */
	@Override
	public void onMessage(Message message) {
		
		TextMessage tm = (TextMessage)message;
		
		try {
			System.out.println("consumer:"+tm.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
