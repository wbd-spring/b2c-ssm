package com.wbd.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wbd.common.utils.CookieUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbUser;
import com.wbd.sso.service.UserService;
/**
 * 拦截器。
 * 该拦截器的作用是获取登录状态下的用户信息。 获取到之后把购物车同步到reids中，
 * 如果用户未登录， 把购物车放入cookie中
 * 
 * 
 * 对购物车所有的请求进行拦截， 前置拦截(preHandle)全部放行,
 * 对购物车的操作有，添加，删除，更新，购物车列表 等等操作，
 * 
 * 
 * 商品添加到购物车，有2种情景，
 *  一种：登录状态， 同步cookie和redis，商品列表放入redis中
 *       删除cookie中的购物车列表，登录状态必须获取用户信息，所以通过过滤器来获取。
 *       
 *       用户登录之后，把uuid生成的token存入redis,key=token值，value=user
 *       ，也把token写入到客户端cookie中，key=token,value=token值
 *       所以登陆的时候可以通过token来获取redis存储的user信息
 *       
 *  另外一种，未登录状态，直接放入cookie中，key=cart value=购物车列表
 * 
 *
* <p>Title: LoginInterceptor.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月9日
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;

	// 前处理，执行handler之前执行此方法。
	//请求之前做的事情，我们在用户把商品加入购物车之前(addCartByItemId), 获取用户信息
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//返回true放行，
		//返回false拦截  
		//1.通过CookieUtils 获取请求cookie中的token信息
		String token = CookieUtils.getCookieValue(request, "token");
		
		//2.如果未登录，直接放行，此时，购物车放入cookie中
		//不能拦截， 因为我们可以在 用户登录与未登录状态下， 都可以把商品添加到购物车，和对购物车删除，更新，添加，等等操作。
		if(StringUtils.isBlank(token)) {
			return true;
		}
		
		//3.如果token存在，就调用sso的服务,通过token获取redis中user信息
		WBDResult wbdResult = userService.getUserByToken(token);
		
		//4.如果用户信息已经过期，直接放行
		if(wbdResult.getStatus()!=200) {
			return true;
		}
		
		//5.如果没有过期，获取用户信息
		TbUser  user = (TbUser) wbdResult.getData();
		
		//6.把user信息放入request中，只需要在controller中判断request是否包含user信息即可
		request.setAttribute("user", user);
		
		System.out.println("preHandle................"+handler.getClass().getName());
		return true;
	}

	//返回渲染视图之前，请求之后做的事情，
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("postHandle................"+handler.getClass().getName());
	}

  //视图渲染之后做的事情，一般用来清理数据，或者垃圾
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		System.out.println("afterCompletion................");
	}

}
