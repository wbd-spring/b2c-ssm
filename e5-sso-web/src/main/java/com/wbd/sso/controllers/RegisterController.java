package com.wbd.sso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 注册控制器
* <p>Title: RegisterController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月7日
 */
@Controller
public class RegisterController {
	
	/**
	 * 跳转到注册页面
	 * <p>Title: showRegister</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	@RequestMapping("/page/register")
	public String showRegister() {
		
		return "register";
	}

}
