package com.wbd.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wbd.common.jedis.JedisClient;
import com.wbd.common.pojo.EasyUIDataGridResult;
import com.wbd.common.utils.IDUtils;
import com.wbd.common.utils.JsonUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.mapper.TbItemDescMapper;
import com.wbd.mapper.TbItemMapper;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbItemDesc;
import com.wbd.pojo.TbItemExample;
import com.wbd.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	// 注入ActiveMQ相关bean
	// 1.利用jmstemplate来发信息

	@Autowired
	private JmsTemplate jmsTemplate;

	// 2.消息发送的 方式， 一对一，或者一对多， 我们采用一对多的方式
	@Autowired
	private Destination topicDestination;

	// 3.注入jedisClient对象

	@Autowired
	private JedisClient jedisClient;

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;

	@Value("${ITEM_BASC}")
	private String ITEM_BASC;

	@Value("${ITEM_DESC}")
	private String ITEM_DESC;

	@Override
	public TbItem getItemById(long itemId) {
		// 查询缓存

		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId +":"+ ITEM_BASC);
			if (StringUtils.isNotBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// 加入redis缓存
		try {

			String json = JsonUtils.objectToJson(item);

			jedisClient.set(ITEM_INFO + ":" + itemId +":"+ ITEM_BASC, json);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {

		// 1.设置分页信息
		PageHelper.startPage(page, rows);
		// 2.执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 3.创建一个返回值对象
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setRows(list);
		// 4.获取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		// 5.设置总记录数
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		return easyUIDataGridResult;
	}

	@Override
	public WBDResult addItem(TbItem item, String desc) {
		long itemId = IDUtils.genItemId();
		item.setId(itemId);
		// 商品状态，1-正常，2-下架，3-删除'
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		itemMapper.insert(item);
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		tbItemDesc.setItemDesc(desc);
		itemDescMapper.insert(tbItemDesc);

		/**
		 * 添加一个商品成功后， 需要同步redis缓存，solr库，库存，等等相关信息， 我们发送商品ID，
		 * 此时我们采用topic的方式进行发送信息,我们先同步solr库为例
		 * 
		 */

		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId + "");
			}
		});

		return WBDResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {

		// 查询缓存

		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId +":"+ ITEM_DESC);
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		TbItemDesc selectByPrimaryKey = itemDescMapper.selectByPrimaryKey(itemId);

		// 加入redis缓存
		try {

			String json = JsonUtils.objectToJson(selectByPrimaryKey);

			jedisClient.set(ITEM_INFO + ":" + itemId +":"+ ITEM_DESC, json);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return selectByPrimaryKey;
	}

}
