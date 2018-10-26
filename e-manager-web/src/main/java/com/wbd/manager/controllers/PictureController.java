package com.wbd.manager.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wbd.common.utils.FastDFSClient;
import com.wbd.common.utils.JsonUtils;

/**
 * 图片上传控制器
* <p>Title: PictureController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月26日
 */
@Controller
public class PictureController 
{
	//spring容器，通过@value注解，获取属性配置文件的值
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL; //注入图片服务器的ip，进行拼接图片地址
	
	/**
	 * @RequestMapping("/pic/upload")
	 * 
	 * @RequestMapping 默认是value,为请求的路径， 如果在请求中
	 * 添加额外的元素需要添加其他的属性,比如字符编码和response的格式
	 * <p>Title: fileUpload</p>  
	 * <p>Description:返回string 类型的json ，为了解决浏览器兼容性问题</p>  
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_XML_VALUE+";charset=utf-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile) {
		//1.加载图片服务器，通过工具类，读取自定义配置服务器地址
		Map<String,Object> resultMap =null;
       try {
		FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
		
		//2.获取文件的扩展名
		String originalFilename = uploadFile.getOriginalFilename();
		String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
		
		//3.通过图片上传工具类，上传文件，返回图片的路径， 图片地址需要与服务器ip进行拼接
		
		String url = fastDFSClient.uploadFile(uploadFile.getBytes(), ext);
		
		url = IMAGE_SERVER_URL+url;
		
		 resultMap = new HashMap<String,Object>();
		
		resultMap.put("error", 0);
		resultMap.put("url", url);
		
		return  JsonUtils.objectToJson(resultMap);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		resultMap.put("error", 1);
		resultMap.put("message", "图片上传失败");
		return JsonUtils.objectToJson(resultMap);
	}
		
	}

}
