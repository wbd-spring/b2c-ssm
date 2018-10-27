package com.wbd.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wbd.common.pojo.EasyUIDataGridResult;
import com.wbd.common.utils.IDUtils;
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

	@Override
	public TbItem getItemById(long itemId) {
		System.out.println("hello world");
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		
		//1.设置分页信息
		PageHelper.startPage(page, rows);
		//2.执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		System.out.println(list.size()+"size:====");
		//3.创建一个返回值对象
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setRows(list);
		//4.获取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		
		//5.设置总记录数
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		return easyUIDataGridResult;
	}

	@Override
	public WBDResult addItem(TbItem item, String desc) {
		long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除'
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
		return WBDResult.ok();
	}

}
