package com.wbd.service.test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 测试利用 jms发消息。
 * 添加一个商品成功后， 需要同步redis缓存，solr库，库存，等等相关信息，此时我们采用topic的方式进行发送信息
* <p>Title: TestSpringActiveMQ.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月1日
 */
public class TestSpringActiveMQ {
	
	
	@Test
	public void  testSpringActiveMQ() {
		
		//1.读取applicationContext-activemq.xml，初始化该文件的bean对象
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		
		// 2.获取jmsTemplate
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		
		// 3.获取 队列/主题， 我们用队列来测试
		Destination destination = (Destination) ac.getBean("queueDestination");
		
		//4.使用 jmstemplate发消息
		
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage("我是tomcat");
			}
		});
		
	}

}
