package com.wbd.sso.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.utils.CookieUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbUser;
import com.wbd.sso.service.UserService;

/**
 * 用户控制器
* <p>Title: UserController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月7日
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService  userService;
	
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	
	/**
	 * 跳转到登录页面
	 * <p>Title: showLogin</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model) {
		//redirect用来 跳转到 之前进来的页面
		model.addAttribute("redirect", redirect);
		return "login";
	}

	/**
	 * 检查用户名是否已经注册
	 * <p>Title: checkData</p>  
	 * <p>Description: </p>  
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public WBDResult checkData(@PathVariable String param,@PathVariable Integer type) 
	
	{
		
		return userService.checkData(param, type);
	}
	
	
	/**
	 * 注册用户
	 * <p>Title: register</p>  
	 * <p>Description: </p>  
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public WBDResult register(TbUser user) {
		
		return userService.createUser(user);
	}
	
	/**
	 * 用户登录成功之后，把token通过response对象写入
	 * 客户端浏览器的cookie中，
	 * cookie也是key与value的形式，
	 * key=token ，key可以随意定义，
	 * value=token具体的值 服务器端uuid生成的
	 * ，
	 * <p>Title: login</p>  
	 * <p>Description: </p>  
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public WBDResult login(String username,String password,HttpServletRequest request, HttpServletResponse response) {
		
		//1.返回token
		WBDResult result =  userService.login(username, password);
		if(result.getStatus()==200) {
			String token  = (String) result.getData();
			
			//2.通过response对象把token写入cookie中
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		
		//3.返回对象中包含token
		return result;
	}
	
	/**
	 * 跨域请求，8088这个控制器要获取8083中js从cookie中获取的token信息，
	 * 这涉及到两个不同的系统，即不同的端口提供的应用服务不一样， 称为js跨域访问
	 * 
	 * 解决方案， 
	 * 1.客户端js请求时，需要声明的请求类型为jsonp类型,采用jquery，jquery已经对jsonp的请求进行了封装
	 * 在 jquery请求中， 修改下面即可。
	 *  dataType : "jsonp",
	 * 2.服务器端方法需要做对应的修改。
	 *  a. 接收callback参数，取回调的js的方法名。
	    b.业务逻辑处理。
        c.响应结果，拼接一个js语句。
	 * 
	 * 通过token获取用户信息：
	 * js通过cookie中获取token信息，然后请求服务器获取对应token在redis存储的user信息
	 * <p>Title: getUserByToken</p>  
	 * <p>Description: </p>  
	 * @param token
	 * @return
	 */
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		
		WBDResult wbdresult = userService.getUserByToken(token);
		//响应结果之前 判断是否为jsonp请求
		if(StringUtils.isNoneBlank(callback)) {
			//把结果封装为一个js语句响应
			MappingJacksonValue mjv = new MappingJacksonValue(wbdresult);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
		
		return wbdresult;
	}
	
	
	
}
