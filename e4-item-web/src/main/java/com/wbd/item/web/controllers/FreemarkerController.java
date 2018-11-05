package com.wbd.item.web.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.wbd.item.web.pojo.Student;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * spring整合freemarker
* <p>Title: FreemarkerController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月5日
 */
@Controller
public class FreemarkerController {
	
	@Autowired
	private FreeMarkerConfigurer  freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genhtml() {
		
		//1.获取configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		
		//2获取模板
		try {
			Template template = configuration.getTemplate("hello.ftl");
			
 //3.创建template需要的数据集，可以是pojo对象，也可以是map
			
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
			
	  //4.创建一个writer对象，利用template与data输出一个html页面
			Writer writer = new FileWriter("d:/freemarker.html");
	  //5.利用template输出， 需要两个参数，一个为 writer，一个是data
			template.process(dataMap, writer);
	  //6.关闭流
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}

}
