package com.wbd.order.service;

import com.wbd.common.utils.WBDResult;
import com.wbd.order.pojo.OrderInfo;

/**
 * 订单接口
* <p>Title: OrderService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月13日
 */
public interface OrderService {
	
	/**
	 * 创建订单,返回WBDResult，包含订单号
	 * <p>Title: createOrder</p>  
	 * <p>Description: </p>  
	 * @param orderInfo
	 * @return
	 */
	public WBDResult createOrder(OrderInfo orderInfo);

}
