package com.wbd.cart.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.cart.service.CartService;
import com.wbd.common.utils.CookieUtils;
import com.wbd.common.utils.JsonUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbUser;
import com.wbd.service.ItemService;

/**
 * 购物车控制器
 * 
 * 解决请求*.html后缀无法返回json数据的问题
        在springmvc中请求*.html不可以返回json数据。
       修改web.xml，添加url拦截格式即可
 * 
* <p>Title: CartController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月8日
 */
@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	
	
	@Autowired
	private CartService cartService;
	
	//cookie的保存时间为7天， 秒为单位
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	/*******************对cookie的操作， 所以不需要业务层**********************************/
	
	
	/**
	 * 商品添加到购物车(cookie),最终把购物车列表添加到cookie中，   key=cart,value=购物车列表
	 * <p>Title: addCartByItemId</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCartByItemId(@PathVariable Long itemId,@RequestParam(defaultValue="1")Integer num,
			
			HttpServletRequest request,HttpServletResponse response) {
		
		/*******登录状态 *******/
		
		//1.先获取user信息， 在拦截器中 已经获取user信息，放入request中
		
		TbUser user = (TbUser) request.getAttribute("user");
		
		//2.如果是登录状态，写入redis
		if(user!=null) {
			
			cartService.addCart(user.getId(), itemId, num);
			
		//3.返回逻辑视图
			
			return "cartSuccess";
			
		}
		
		/*******未登录状态 *******/
		
		//1.cookie中取商品列表
		List<TbItem> list = getCartListFromCookie(request);
		
		//2.判断商品是否存在，如果存在，数量相加
		boolean flag = false;
		for (TbItem tbItem : list) {
			if(tbItem.getId() ==itemId.longValue() ) {
				//如果cookie存在，数量相加
				tbItem.setNum(tbItem.getNum()+num);
				flag = true; //存在就修改为true
				break; //这里最多只能循环一次，因为我们添加商品， 是一件一件的往购物车中添加的，
			}
		}
		
		//3.如果不存在， 就根据商品id查询商品信息，获取到TbItem对象。
		if(!flag) {
			
			TbItem tbItem = itemService.getItemById(itemId);
			 //设置商品数量
			tbItem.setNum(num);
			
			 //获取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNoneBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			
		    // 把商品添加到商品列表
			list.add(tbItem);
			
		}
		
		//4.商品列表写入cookie
		
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);
		
		return "cartSuccess";
	}
	
	/**
	 * 从cookie中获取购物车列表
	 * <p>Title: getCartListFromCookie</p>  
	 * <p>Description: </p>  
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		
		
		//1.从cookie中获取购物车列表
		List<TbItem> list = new ArrayList<TbItem>();
		
		String json = CookieUtils.getCookieValue(request, "cart",true);
		//如果cookie中没有值， 直接返回空的list，
		if(StringUtils.isBlank(json)) {
			return list;
		}
		
		
		//如果cookie中有值， 把json转换为List<TbItem>
		list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 从cookie中获取购物车列表
	 * <p>Title: showCartList</p>  
	 * <p>Description: </p>  
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		
		//从cookie中获取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		
		/************登录状态***/
		
	   //1.先获取user信息， 在拦截器中 已经获取user信息，放入request中
		
		TbUser user = (TbUser) request.getAttribute("user");
		
		if(user!=null) {
			
			//合并cookie和redis中购物车的数据
			cartService.mergeCart(user.getId(), cartList);
			
			//把cookie中的购物车数据删除
			CookieUtils.deleteCookie(request, response, "cart");
			
			//根据userid，再重新获取redis中用户购物车列表数据
			cartList = 	cartService.getCartList(user.getId());
			//返回逻辑视图
			
		}
		
		/************未登录状态***/
		//未登录状态下
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 根据商品id和数量 更新cookie中的购物车列表
	 * <p>Title: updateCartNum</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/cart/update/num/{itemId}/{num}",method=RequestMethod.POST)
	@ResponseBody
	public WBDResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request, HttpServletResponse response) {
		
		/************登录状态***/
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			
		return	cartService.updateCartNum(user.getId(), itemId, num);
			
		}
		
		/************未登录状态***/
		//1.从request中获取购物车列表
		List<TbItem> list = this.getCartListFromCookie(request);
		
		//2.循环迭代购物车列表，找到对应的商品，修改数量
		for (TbItem tbItem : list) {
			if(tbItem.getId()== itemId.longValue()) {
				tbItem.setNum(null);
				break;
			}
		}
		
		
		//3.把更新后的商品列表再写入cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);

		return WBDResult.ok();
	}

	
	@RequestMapping("/cart/delete/{itemId}")
	public String  deletCartItem(@PathVariable Long itemId,HttpServletRequest request, HttpServletResponse response) {
		
		/************登录状态***/
		TbUser user = (TbUser) request.getAttribute("user");
		
		if(user!=null) {
				cartService.deleteCart(user.getId(), itemId);
				return "redirect:/cart/cart.html";
		}
		
		
		/************未登录状态***/
		//1.获取购物车列表
		List<TbItem> list = this.getCartListFromCookie(request);
		//2.在列表中找到对应的商品,删除
		for (TbItem tbItem : list) {
			if(tbItem.getId()== itemId.longValue()) {
				list.remove(tbItem);
				break;
			}
		}
		//3.把更新后的商品列表再写入cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);

		return "redirect:/cart/cart.html";
	}
}
