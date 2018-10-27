package com.wbd.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.pojo.EasyUIDataGridResult;
import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbItem;
import com.wbd.service.ItemService;
/**
 * 
 * 商品控制器
* <p>Title: ItemController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月24日
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据商品id查询商品信息
	 * <p>Title: getItemById</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	/**
	 * 分页查询
	 * <p>Title: getItemList</p>  
	 * <p>Description: </p>  
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
	
      return itemService.getItemList(page, rows);
	}
	
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public WBDResult addItem(TbItem item, String desc) {
		return itemService.addItem(item, desc);
	}
	
}
