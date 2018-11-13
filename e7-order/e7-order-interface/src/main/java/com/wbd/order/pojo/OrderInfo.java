package com.wbd.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.wbd.pojo.TbOrder;
import com.wbd.pojo.TbOrderItem;
import com.wbd.pojo.TbOrderShipping;

/**
 * 该pojo，给提交订单页面使用，
* <p>Title: OrderInfo.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月13日
 */
public class OrderInfo extends TbOrder implements Serializable{

	
	/** serialVersionUID*/ 
	private static final long serialVersionUID = 1L;

	//订单页面需要的参数，订单与商品的详细列表
	private List<TbOrderItem> orderItems;
	
	//订单页面需要的参数，订单与物流的详细列表
	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
	
	
	
	
}
