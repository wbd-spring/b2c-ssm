package com.wbd.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wbd.cart.service.CartService;
import com.wbd.common.utils.CookieUtils;
import com.wbd.common.utils.JsonUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbUser;
import com.wbd.sso.service.UserService;

/**
 * 拦截器，拦截用户登录的信息，如果登录状态， 获取用户信息，如果未登录， 跳转到
 * 登录页面，登录成功之后返回到之前的页面
* <p>Title: LoginInterceptor.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月12日
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService  userService;
	
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//1.通过cookie获取token
		String token = CookieUtils.getCookieValue(request, "token");
		
		if(StringUtils.isBlank(token)) {
			//如果未登录， 跳转到登录页面，登录成功之后，跳转到当前url中
			System.out.println("请求的url="+request.getRequestURL());
			response.sendRedirect(SSO_URL+ "/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		//2.如果登录状态，通过token调用sso系统服务获取用户信息
		 WBDResult result = userService.getUserByToken(token);
		 
		 if(result.getStatus()!=200) 
		 {
			 
			 //如果token不存在，或者过期，跳转到登录页面，登录成功之后，跳转到当前url中
			 
			response.sendRedirect(SSO_URL+ "/page/login?redirect=" + request.getRequestURL());
           //必须在sso系统中 修改 showLogin方法 把redirect传入到页面中。
			//拦截
			return false;
		 }
		 
		//如果取到用户信息，是登录状态，需要把用户信息写入request。
		 TbUser user = (TbUser) result.getData();
		 request.setAttribute("user", user);
		 
		 //判断cookie中是否有购物车的数据， 如果有就合并到redis中。
		 String string = CookieUtils.getCookieValue(request, "cart",true);
		 if(StringUtils.isNotBlank(string)) {
			 
			 //调用合并购物车的方法
			 cartService.mergeCart(user.getId(), JsonUtils.jsonToList(string, TbItem.class));
		 }
		 
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
