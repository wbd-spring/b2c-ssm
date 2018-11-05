package com.wbd.item.web.mq;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.wbd.item.web.pojo.Item;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbItemDesc;
import com.wbd.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 添加商品时，利用mq发送商品id生成对应的页面(利用freemarker模板）
 * 1,利用mq监听器获取添加商品时mq发布的id信息，
 * 2，通过商品id，查询商品信息， 所以必须调用商品服务接口
 * 3.将查询到的商品信息，通过freemarker模板输出html页面
* <p>Title: GenHtmlListener.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月5日
 */
public class GenHtmlListener implements MessageListener{
	
	//注入freemarker对象
	@Autowired
	private FreeMarkerConfigurer  freeMarkerConfigurer;
	
	@Autowired
	private ItemService itemService;
	
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;

	@Override
	public void onMessage(Message message) {
		//1.强制转换
		TextMessage  tm = (TextMessage) message;
		//2.通过mq 监听器获取信息
		try {
			Long itemId = new Long(tm.getText());
			
	
			//等到 添加商品的事物提交，当商品事物提交成功之后， 然后再发送mq信息，
			//mq发送完毕，我们这边采用用mq监听器获取到信息，才能进行下一步操作
			Thread.sleep(1000);
			
			//根据商品id获得商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			
			Item item = new Item(tbItem);
			
			//根据商品id获取商品描述
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("itemDesc", itemDesc);
			dataMap.put("item", item);
			
			//3.利用freemarker 获取template对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			
			Template template = configuration.getTemplate("item.ftl");
			
			//4.定义输出流，文件的输出目录
			
			Writer writer = new FileWriter(HTML_GEN_PATH+itemId+".html");
			
			template.process(dataMap, writer);
			writer.close();
			
		} catch (JMSException | InterruptedException | IOException | TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
