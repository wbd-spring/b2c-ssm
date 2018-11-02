package com.wbd.search.service;

import java.io.IOException;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * 加载applicationContext-activemq.xml文件
 * 初始化文件中所有的bean
* <p>Title: TestSpringActiveMQ.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月1日
 */
public class TestSpringActiveMQ {

	
	
	@Test
	public void init() {
		
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
	   try {
		   System.out.println("c....");
		System.in.read();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
}
