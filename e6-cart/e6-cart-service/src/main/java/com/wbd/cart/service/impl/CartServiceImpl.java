package com.wbd.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wbd.cart.service.CartService;
import com.wbd.common.jedis.JedisClient;
import com.wbd.common.utils.JsonUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.mapper.TbItemMapper;
import com.wbd.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient  jedisClient;
	
	//存入 redis中购物车商品的前缀
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	//向redis中添加购物车的商品
	@Override
	public WBDResult addCart(long userId, long itemId, int num) {
		
		//1.redis 中存储的购物车商品的信息为hset：
		  // key=REDIS_CART_PRE:userId,filed=itemId,value=商品信息
		
		//2.判断商品是否存在
		boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId+"");
		
		//3.如果存在数量相加
		if(hexists){
			
		   //3.1获取商品信息，
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		  
			//3.2转换为对象
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			
			//3.4设置数量
			tbItem.setNum(tbItem.getNum()+ num);
			
			//3.5再次写回redis,
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
			
			return WBDResult.ok();
		}
		
		//4.如果不存在，通过商品id获取商品信息，然后写入redis
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
			  //4.1设置数量  
			 item.setNum(num);
			  //4.2只获取一张图片
			 String image = item.getImage();
			 if(StringUtils.isNotBlank(image)) {
				 
				 item.setImage(image.split(",")[0]);
			 }
			 
		//5.写入redis
			 jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return WBDResult.ok();
	}

	/**
	 * 把cookie中的购物车列表 ，合并到redis中，
	 * 其实可以直接循环调用addCart方法 
	 */
	@Override
	public WBDResult mergeCart(long userId, List<TbItem> cartList) {
		
		
		if(cartList.size()>0) {
		for (TbItem tbItem : cartList) {
			this.addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		
		return WBDResult.ok();
		}
		return WBDResult.build(400, "合并购物车失败");
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		//hvals 获取hset中 所有key对应values的值，不是field,我们需要获取所有用户购物车列表，所有必须用该方法
		/**
		 * 购物车在redis存储的格式如下
		 *  
		 * key=cart:userid, field=itemid=154140898198890 value=item
		 * key=cart:userid, field=itemid=981821 value=item
		 * 
		 * 返回所有的value，为json字符串， 其实是商品信息， 把json再转换我商品信息
		 */
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		for (String json : jsonList) {
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			itemList.add(tbItem);
		}
		
		
		return itemList;
	}

	@Override
	public WBDResult updateCartNum(long userId, long itemId, int num) {
		//1.从redis中获购物车某个商品的信息
		String hget = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		
		//2.转换为对象
		TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
		
		//3.更新数量
		
		tbItem.setNum(num);
		
		//4.再次写入redis
		
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"",JsonUtils.objectToJson(tbItem));
		
		return WBDResult.ok();
	}

	@Override
	public WBDResult deleteCart(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE+":"+userId, itemId+"");
		return WBDResult.ok();
	}

}
