package com.wbd.service.test;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

/**
 * 消息中间件： 消息传递的方式有2中： 1.消息队列模式：， 一对一的方式，一个消息提供者， 一个消费者 2.发布/订阅者模式 ：topic模式，
 * 一个发布者，多个消费者
 * 
 * <p>
 * Title: TestActiveMQ.java
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 朱光和
 * @date 2018年10月31日
 */
public class TestActiveMQ {

	// activemq web的http端口为8186，tcp通讯端口为61616

	private String brokerURL = "tcp://118.190.71.215:61616";

	/**
	 * queue队列模式 创建消息服务提供者
	 * <p>
	 * Title: createPruduce
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 */
	@Test
	public void createQueuePruduce() {

		// 1.创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

		// 2.创建连接

		try {
			Connection connection = factory.createConnection();
			
			connection.start();//必须开启连接

			// 3.创建session,不开启事物， 客户与activemq连接时，不开启事物， 自动确认需要开启
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 4.创建队列，给队列命名 ，队列模式只能做消息的一对一的传输
			Queue wbdqueue = session.createQueue("wbd-queue");

			// 5.创建消息提供者
			MessageProducer messageProducer = session.createProducer(wbdqueue);

			// 6.创建需要发送的消息对象

			TextMessage message = new ActiveMQTextMessage();

			message.setText("我是activemq");

			// 7.消息提供者发送消息

			messageProducer.send(message);

			// 8.关闭资源
			messageProducer.close();
			session.close();
			connection.close();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
	 * 主题:发布与订阅模式，生产者
	 * <p>Title: createTopicPruduce</p>  
	 * <p>Description: </p>
	 */
	@Test
	public void createTopicPruduce() {

//		 1.创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

		// 2.创建连接

		try {
			Connection connection = factory.createConnection();
			connection.start();//必须开启连接

		// 3.创建session,不开启事物， 客户与activemq连接时，不开启事物， 自动确认需要开启
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// 4.创建主题模式， 采用发布与订阅模式， 一个消息可以发送给多个消费者
		Topic topic = session.createTopic("topic-test");

			// 5.创建消息提供者
			MessageProducer messageProducer = session.createProducer(topic);
			

			// 6.创建需要发送的消息对象

			TextMessage message = new ActiveMQTextMessage();

			message.setText("test topic");

			// 7.消息提供者发送消息

			messageProducer.send(message);

			// 8.关闭资源
			messageProducer.close();
			session.close();
			connection.close();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	/**
	 * 队列 1对一，消费者
	 * 
	 * <p>
	 * Title: createConsumer
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 */
	@Test
	public void createQueueConsumer() {

		// 1.创建工厂对象
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

		Connection conn = null;
		Session session = null;
		MessageConsumer consumer = null;

		try {
			// 2.创建连接
			conn = factory.createConnection();
			
			conn.start();//必须开启连接

			// 3.创建session
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 4.创建队列
			Queue queue = session.createQueue("wbd-queue");

			// 5.创建消费者
			consumer = session.createConsumer(queue);

			// 6.消费者 监听消息
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {

					TextMessage tm = (TextMessage) message;
					try {
						System.out.println("message=" + tm.getText());

					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 等待键盘输入
		try {
			System.in.read();
			consumer.close();
			session.close();
			conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 主题：发布/与订阅 消费者
	 * <p>Title: createTopicConsumer</p>  
	 * <p>Description: </p>
	 */
	@Test
	public void createTopicConsumer() {

		// 1.创建工厂对象
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

		Connection conn = null;
		Session session = null;
		MessageConsumer consumer = null;

		try {
			// 2.创建连接
			conn = factory.createConnection();
			
			conn.start();//必须开启连接

			// 3.创建session
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 4.创建队列
			Topic topic = session.createTopic("topic-test");

			// 5.创建消费者
			consumer = session.createConsumer(topic);

			// 6.消费者 监听消息
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {

					TextMessage tm = (TextMessage) message;
					try {
						System.out.println("message=" + tm.getText());

					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 等待键盘输入
		try {
			System.in.read();
			consumer.close();
			session.close();
			conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
