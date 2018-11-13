package com.wbd.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wbd.common.jedis.JedisClient;
import com.wbd.common.utils.WBDResult;
import com.wbd.mapper.TbOrderItemMapper;
import com.wbd.mapper.TbOrderMapper;
import com.wbd.mapper.TbOrderShippingMapper;
import com.wbd.order.pojo.OrderInfo;
import com.wbd.order.service.OrderService;
import com.wbd.pojo.TbOrderItem;
import com.wbd.pojo.TbOrderShipping;

/**
 * 创建订单实现类，
 * 订单编号，通过redis的自增来实现 ，incr自增，decr自减。
 * incr自增，decr自减。 可以先不需要set,直接incr,decr,
 * 即使key不存在，就默认从0开始
* <p>Title: OrderServiceImpl.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月13日
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private JedisClient  jedisClient;
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private  TbOrderShippingMapper  orderShippingMapper;
	
	//订单的key
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	//订单的key的起始位置
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	
	//订单商品表的 主键id 的key
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	
	/**
	 * 订单编号通过redis 生成
	 * 
	 * 
	 */
	@Override
	public WBDResult createOrder(OrderInfo orderInfo) {
		
		//1.使用redis incr生成订单编号
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			//设置key，设置起始位置
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		
		//2.通过incr获取订单id
		String orderId=jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		orderInfo.setOrderId(orderId);
		
			//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insert(orderInfo);
		
		//3. 向订单明细表插入数据。
		 //3.1获取通过页面传递过来的订单信息
		List<TbOrderItem> orderItems = orderInfo.getOrderItems(); 
		
		for (TbOrderItem tbOrderItem : orderItems) {
			
		   //3.2生成商品明细的id
			String detailId=jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			
			tbOrderItem.setId(detailId);
			tbOrderItem.setOrderId(orderId);
			orderItemMapper.insert(tbOrderItem);
		}
		
		//向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		//返回WBDResult，包含订单号
		return WBDResult.ok(orderId);
		
	}

}
