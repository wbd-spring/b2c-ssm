package com.wbd.item.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wbd.item.web.pojo.Student;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemarker模板语言
 * 1.需要一个模板
 * 2.需要data
 * 3.输出文件。
* <p>Title: TestFreemarker.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月5日
 */
public class TestFreemarker {
	
	
	
	
	@Test
	public void testFreemarker() {
		//1.创建configuration对象,参数为版本， 就是freemarker对应的版本
		Configuration configuration  = new Configuration(Configuration.getVersion());
		
		//2.设置模板的路径父目录
		try {
			configuration.setDirectoryForTemplateLoading(new File("F:\\git-workspace\\b2c-ssm\\e4-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
			
		//3.设置模板的字符集
			configuration.setDefaultEncoding("utf-8");
			
	   // 4.加载模板，创建一个模板对象
			
			Template template = configuration.getTemplate("hello.ftl");
			
	   //5.创建template需要的数据集，可以是pojo对象，也可以是map
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			
			Student  stu = new Student(1,"张三",20,new Date());
			Student  stu2 = new Student(2,"李四",21,new Date());
			Student  stu3 = new Student(3,"王五",22,new Date());
			Student  stu4 = new Student(4,"赵六",23,new Date());
			Student  stu5 = new Student(5,"前七",24,new Date());
			List<Student> list  = new ArrayList<Student>();
			list.add(stu);
			list.add(stu2);
			list.add(stu3);
			list.add(stu4);
			list.add(stu5);
			
			
			dataMap.put("freemarker", "test freemarker");
			dataMap.put("stu", stu);
			dataMap.put("stuList", list);
			dataMap.put("abc", "ef");
			
	  //6.创建一个writer对象，利用template与data输出一个html页面
			
			Writer writer = new FileWriter("d:/freemarker.html");
			
			
	  //7.利用template输出， 需要两个参数，一个为 writer，一个是data
			template.process(dataMap, writer);
			
	  //8.关闭流
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
