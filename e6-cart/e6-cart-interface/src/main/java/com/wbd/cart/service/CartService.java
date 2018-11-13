package com.wbd.cart.service;

import java.util.List;

import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbItem;

/**
 * 购物车服务
* <p>Title: CartService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月9日
 */
public interface CartService {
	
	/**
	 * 将购物车添加到redis中，以hset形式存储
	 * key=redis_cart_pre:userid
	 * field=itemid
	 * value=item
	 * <p>Title: addCart</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public WBDResult addCart(long userId,long itemId,int num);
	
	/**
	 * 合并购物车
	 * 把cookie中的购物车列表合并到redis中
	 * 
	 * <p>Title: mergeCart</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @param cartList
	 * @return
	 */
	public WBDResult mergeCart(long userId,List<TbItem> cartList);
	
	/**
	 * 根据用户id获取redis中的购物车列表
	 * <p>Title: getCartList</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @return
	 */
	public List<TbItem> getCartList(long userId);
	
	
	/**
	 * 更新购物车商品的数量
	 * <p>Title: updateCartNum</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public WBDResult updateCartNum(long userId,long itemId,int num);
	
	/**
	 * 删除购物车的商品
	 * <p>Title: deleteCart</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public WBDResult deleteCart(long userId,long itemId);
	
	/**
	 * 根据用户id清除购物车
	 * <p>Title: clearCart</p>  
	 * <p>Description: </p>  
	 * @param userId
	 * @return
	 */
	public WBDResult clearCart(long userId);

}
