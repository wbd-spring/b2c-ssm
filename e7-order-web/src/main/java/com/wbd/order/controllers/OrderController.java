package com.wbd.order.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wbd.cart.service.CartService;
import com.wbd.common.utils.WBDResult;
import com.wbd.order.pojo.OrderInfo;
import com.wbd.order.service.OrderService;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbUser;
/**
 * 订单控制器
* <p>Title: OrderController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月12日
 */
@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 跳转到订单页面
	 * <p>Title: showOrderCart</p>  
	 * <p>Description: </p>  
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		
		TbUser user = (TbUser) request.getAttribute("user");
		
		//通过用户id获取redis中的购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		
		request.setAttribute("cartList", cartList);
		
		return "order-cart";
	}
	
	
	/**
	 * 创建(提交)订单
	 * <p>Title: showOrderCart</p>  
	 * <p>Description: </p>  
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createrOrder(OrderInfo orderInfo,HttpServletRequest request) {
		
		//1.获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		
		//2.把用户信息填充到orderInfo对象中
		
		orderInfo.setBuyerNick(user.getUsername());
		orderInfo.setUserId(user.getId());
		
		//3.调用服务生成订单
		
		WBDResult wbdResult = orderService.createOrder(orderInfo);
		
		//4.订单生成成功，需要清除redis中的购物车
		if(wbdResult.getStatus()==200) {
			cartService.clearCart(user.getId());
		}
		
		//5.把订单号传递给页面
		request.setAttribute("orderId", wbdResult.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		
		return "success";
	}
	
}
